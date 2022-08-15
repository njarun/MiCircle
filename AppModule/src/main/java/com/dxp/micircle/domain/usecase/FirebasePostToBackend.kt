package com.dxp.micircle.domain.usecase

import android.content.Context
import androidx.work.*
import com.dxp.micircle.Config
import com.dxp.micircle.data.router.CoroutineDispatcherProvider
import com.dxp.micircle.domain.helpers.NewPostObserver
import com.dxp.micircle.domain.router.model.PostModel
import com.dxp.micircle.domain.router.repository.FeedsRepository
import com.dxp.micircle.domain.router.repository.PostsRepository
import com.dxp.micircle.domain.worker.PostUploadWorker
import com.dxp.micircle.utils.Constants
import com.dxp.micircle.utils.Utility
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class FirebasePostToBackend @Inject constructor(private val workManager: WorkManager,
    private val postsRepository: PostsRepository, private val feedsRepository: FeedsRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider) {

    @Inject
    lateinit var firebaseFirestore: FirebaseFirestore

    @Inject
    lateinit var newPostObserver: NewPostObserver

    @Inject @ApplicationContext
    lateinit var context: Context

    fun execute(postModel: PostModel) = flow {

        try {

            if(postModel.mediaList.isNullOrEmpty() && Utility.isNetworkAvailable(context)) { //context just cos of firebase not throwing damn exception! -- Improvise in next

                val postRef = firebaseFirestore.collection(Config.FBD_POSTS_PATH)
                    .document(postModel.postId)

                postModel.timestamp = postModel.timestamp
                val postTask = postRef.set(postModel)
                Tasks.await(postTask)

                if(!postTask.isSuccessful)
                    throw postTask.exception ?: Exception("-1")

                feedsRepository.processNewPostToFeed(postModel)

                newPostObserver.publish(postModel)

                emit(1)
            }
            else {

                postsRepository.savePost(postModel)
                feedsRepository.processNewPostToFeed(postModel)

                val data = Data.Builder()
                    .putString(Constants.EXTRA_POST_ID, postModel.postId)
                    .putString(Constants.EXTRA_USER_ID, postModel.userId)
                    .build()

                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val postUploadWorker = OneTimeWorkRequestBuilder<PostUploadWorker>()
                    .setInputData(data)
                    .setConstraints(constraints)
                    .build()

                workManager.enqueue(postUploadWorker)

                emit(0)
            }
        }
        catch (e: Exception) {

            throw e
        }
    }
    .flowOn(coroutineDispatcherProvider.IO())
}