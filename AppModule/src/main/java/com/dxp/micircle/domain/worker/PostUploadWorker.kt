package com.dxp.micircle.domain.worker

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.dxp.micircle.Config
import com.dxp.micircle.R
import com.dxp.micircle.domain.router.repository.PostsRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.io.File

@HiltWorker
class PostUploadWorker @AssistedInject constructor(@Assisted val context: Context,
@Assisted val params: WorkerParameters, private val postsRepository: PostsRepository,
private val firebaseDatabase: FirebaseDatabase, private val firebaseStorage: FirebaseStorage) : CoroutineWorker(context, params) {

    private val TAG = this::class.java.simpleName

    override suspend fun doWork(): Result {

        try {

            Timber.d("Hello From Worker")

            setForeground(createForegroundInfo())

            val postId = inputData.getString("POST_ID")

            postId?.let {

                val post = postsRepository.getPost(postId)

                post?.let { postModel ->

                    val postRef = firebaseDatabase.reference.child(Config.FBD_POSTS_PATH)
                        .child(postId)

                    val postMediaRef = firebaseStorage.reference
                        .child(postModel.userId)
                        .child(Config.FBS_POSTS_MEDIA_PATH)
                        .child(postId)

                    postModel.mediaList?.forEach {

                        val file = Uri.fromFile(File(it.url))
                        val mediaRef = postMediaRef.child("${it.mediaId}.${file.lastPathSegment}")

                        val uploadTask = mediaRef.putFile(file)
                        Tasks.await(uploadTask)

                        if(uploadTask.isSuccessful) {

                            val urlTask = mediaRef.downloadUrl
                            Tasks.await(urlTask)
                            if(urlTask.isSuccessful)
                                it.url = urlTask.result.toString()
                            else throw urlTask.exception ?: Exception("-1")
                        }
                        else throw uploadTask.exception ?: Exception("-1")
                    }

                    val postTask = postRef.setValue(postModel)
                    Tasks.await(postTask)

                    if(!postTask.isSuccessful)
                        throw postTask.exception ?: Exception("-1")

                    postsRepository.deletePost(postId)
                }
            }
        }
        catch (e: Exception) {

            return Result.failure()
        }

        return Result.success()
    }

    private fun createForegroundInfo(): ForegroundInfo {

        val notificationId = (1023..11589).random()
        return ForegroundInfo(notificationId, createNotification())
    }

    private fun createNotification(): Notification {

        val intent = WorkManager.getInstance(context).createCancelPendingIntent(id)

        val builder = NotificationCompat.Builder(context, "channelId")
            .setContentTitle(context.getString(R.string.app_name))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNotificationChannel().also {
                builder.setChannelId(it.id)
            }
        }

        return builder.build()
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {

        return NotificationChannel(TAG, context.getString(R.string.post_upload), NotificationManager.IMPORTANCE_HIGH).also { channel ->

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}