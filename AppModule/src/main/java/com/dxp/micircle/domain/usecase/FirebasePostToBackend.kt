package com.dxp.micircle.domain.usecase

import android.net.Uri
import androidx.work.*
import com.dxp.micircle.data.router.CoroutineDispatcherProvider
import com.dxp.micircle.domain.router.model.PostModel
import com.dxp.micircle.domain.router.repository.PostsRepository
import com.dxp.micircle.domain.worker.PostUploadWorker
import com.dxp.micircle.utils.Constants
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class FirebasePostToBackend @Inject constructor(private val workManager: WorkManager,
    private val postsRepository: PostsRepository, private val coroutineDispatcherProvider: CoroutineDispatcherProvider) {

    fun executeWorker(postModel: PostModel) = flow {

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

        emit(false)
    }
    .flowOn(coroutineDispatcherProvider.IO())

    fun execute(postModel: PostModel, postRef: DatabaseReference, postMediaRef: StorageReference) = flow {

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

        if(postTask.isSuccessful)
            emit(false)
        else throw postTask.exception ?: Exception("-1")
    }
    .flowOn(coroutineDispatcherProvider.IO())
}