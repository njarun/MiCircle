package com.dxp.micircle.domain.usecase

import androidx.work.*
import com.dxp.micircle.Config
import com.dxp.micircle.data.router.CoroutineDispatcherProvider
import com.dxp.micircle.domain.router.model.PostModel
import com.dxp.micircle.domain.router.repository.PostsRepository
import com.dxp.micircle.domain.worker.PostUploadWorker
import com.dxp.micircle.utils.Constants
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class FirebasePostToBackend @Inject constructor(private val workManager: WorkManager,
    private val postsRepository: PostsRepository, private val coroutineDispatcherProvider: CoroutineDispatcherProvider) {

    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase

    fun execute(postModel: PostModel) = flow {

        try {

            if(postModel.mediaList.isNullOrEmpty()) {

                val postRef = firebaseDatabase.reference.child(Config.FBD_POSTS_PATH)
                    .child(postModel.postId)

                val postTask = postRef.setValue(postModel)
                Tasks.await(postTask)

                if(!postTask.isSuccessful)
                    throw postTask.exception ?: Exception("-1")

                emit(1)
            }
            else {

                postsRepository.savePost(postModel)

                val data = Data.Builder()
                    .putString(Constants.EXTRA_POST_ID, postModel.postId)
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