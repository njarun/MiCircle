package com.dxp.micircle.domain.worker

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.dxp.micircle.Config
import com.dxp.micircle.R
import com.dxp.micircle.domain.helpers.NewPostObserver
import com.dxp.micircle.domain.router.repository.PostsRepository
import com.dxp.micircle.utils.Constants
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.google.firebase.storage.FirebaseStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.io.File
import java.util.*

@HiltWorker @Suppress("BlockingMethodInNonBlockingContext")
class PostUploadWorker @AssistedInject constructor(@Assisted val context: Context, @Assisted val params: WorkerParameters,
private val firebaseAuth: FirebaseAuth, private val postsRepository: PostsRepository, private val FirebaseFirestore: FirebaseFirestore,
private val firebaseStorage: FirebaseStorage, private val newPostObserver: NewPostObserver) : CoroutineWorker(context, params) {

    private val notiChannelId = Constants.NEW_POST_WORKER_NOTIFICATION_CHANNEL_ID
    private val notiId = (1023..11589).random()
    private lateinit var mBuilder: NotificationCompat.Builder

    override suspend fun doWork(): Result {

        try {

            Timber.d("Hello from ${context.getString(R.string.app_name)} Worker") // :D

            setForeground(createForegroundInfo())

            val postId = inputData.getString(Constants.EXTRA_POST_ID)
            val userId = inputData.getString(Constants.EXTRA_USER_ID)

            if(validUser(userId) && postId != null) {

                postsRepository.getPost(postId)?.let { postModel ->

                    updateProgress(0, true)

                    val postRef = FirebaseFirestore.collection(Config.FBD_POSTS_PATH)
                        .document(postId)

                    val postMediaRef = firebaseStorage.reference
                        .child(postModel.userId)
                        .child(Config.FBS_POSTS_MEDIA_PATH)
                        .child(postId)

                    var currentMedia = 0

                    postModel.mediaList?.forEach { mediaModel ->

                        try {

                            if(validUser(userId)) {

                                updateProgress(getProgress(++currentMedia, postModel.mediaList.size), false)

                                if(!mediaModel.url.contains("firebasestorage.googleapis.com")) { //Check if already uploaded - @Todo Move this to a flag and remove the url check -- nj

                                    val file = File(mediaModel.url)

                                    if(file.exists()) { //Check file exists at the time of upload

                                        val fileUri = Uri.fromFile(file)
                                        val mediaRef = postMediaRef.child("${mediaModel.mediaId}.${fileUri.lastPathSegment}")

                                        val uploadTask = mediaRef.putFile(fileUri)
                                        Tasks.await(uploadTask)

                                        if(uploadTask.isSuccessful) {

                                            val urlTask = mediaRef.downloadUrl
                                            Tasks.await(urlTask)
                                            if(urlTask.isSuccessful)
                                                mediaModel.url = urlTask.result.toString()
                                            else throw urlTask.exception ?: Exception("-1")
                                        }
                                        else throw uploadTask.exception ?: Exception("-1")

                                        postsRepository.updateMedia(mediaModel)
                                    }
                                }
                            }
                        }
                        catch (e: Exception) {
                            throw e
                        }
                    }

                    if(validUser(userId)) {

                        updateProgress(100, true)

                        postModel.timestamp = postModel.timestamp
                        val postTask = postRef.set(postModel)
                        Tasks.await(postTask)

                        if(!postTask.isSuccessful)
                            throw postTask.exception ?: Exception("-1")

                        postsRepository.deletePost(postId)

                        newPostObserver.publish(postModel)

                        updateProgress(100, false)
                    }
                }
            }
        }
        catch (e: Exception) { //Handle this! -- Also inform user :D

            if(e !is FirebaseNoSignedInUserException) {
                return Result.failure()
            }
        }

        return Result.success()
    }

    private fun validUser(userId: String?) : Boolean {
        return userId != null && firebaseAuth.currentUser != null && userId == firebaseAuth.currentUser!!.uid
    }

    private fun createForegroundInfo(): ForegroundInfo {

        return ForegroundInfo(notiId, createNotification())
    }

    private fun createNotification() : Notification {

        val cancelIndent = WorkManager.getInstance(context).createCancelPendingIntent(id) //May be later! :D

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val notificationId = Random().nextInt()
        val isOs11Plus = Build.VERSION.SDK_INT >= 31
        val flags = if (isOs11Plus) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        val contentIntent = PendingIntent.getActivity(context, notificationId, intent, flags)

        val title = context.getString(R.string.app_name)
        val message = context.getString(R.string.posting_to_your_timeline)

        mBuilder = NotificationCompat.Builder(context, notiChannelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.ic_launcher_round
                    )
                )
                .setContentIntent(contentIntent)
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(contentIntent)
                .setPriority(getNotificationImportance()) /*channelDetails.channelImportance*/
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setChannelId(notiChannelId)
                .setProgress(0, 100, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNotificationChannel().also {
                mBuilder.setChannelId(it.id)
            }
        }

        val notificationStyle = NotificationCompat.BigTextStyle().setBigContentTitle(title)
            .bigText(message)

        mBuilder.setStyle(notificationStyle)

        val notification = mBuilder.build()

        notification.defaults = notification.defaults or Notification.DEFAULT_VIBRATE
        notification.defaults = notification.defaults or Notification.DEFAULT_LIGHTS

        return notification
    }

    private fun getNotificationImportance(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            NotificationManager.IMPORTANCE_MAX
        else NotificationManager.IMPORTANCE_DEFAULT
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {

        return NotificationChannel(notiChannelId,
            context.getString(R.string.post_upload), NotificationManager.IMPORTANCE_HIGH).also { channel ->

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getProgress(currentMedia: Int, totalMedia: Int): Int {
        return ((currentMedia.toDouble()/totalMedia) * 100).toInt()
    }

    private fun updateProgress(progress: Int, indeterminate: Boolean) {

        Timber.d("Progress - $progress")

        mBuilder.setProgress(100, progress, indeterminate)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notiId, mBuilder.build())
    }
}