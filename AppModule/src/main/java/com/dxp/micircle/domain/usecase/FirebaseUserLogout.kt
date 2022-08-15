package com.dxp.micircle.domain.usecase

import androidx.work.WorkManager
import com.dxp.micircle.data.router.CoroutineDispatcherProvider
import com.dxp.micircle.domain.router.repository.FeedsRepository
import com.dxp.micircle.domain.router.repository.PostsRepository
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FirebaseUserLogout @Inject constructor(private val firebaseAuth: FirebaseAuth,
                             private val coroutineDispatcherProvider: CoroutineDispatcherProvider) {

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var postsRepository: PostsRepository

    @Inject
    lateinit var feedsRepository: FeedsRepository

    operator fun invoke() : Single<Boolean> {

        val subject = SingleSubject.create<Boolean>()
        return subject.doOnSubscribe {

            try {

                firebaseAuth.addAuthStateListener {
                    it.currentUser?.let { subject.onSuccess(false) } ?: subject.onSuccess(true)
                }

                firebaseAuth.signOut()

                //Todo - Get the current WM post reference and delete the post from server -- nj
            }
            catch (e: Exception) {

                subject.onError(e)
            }
        }
    }

    fun clearUserData() = flow {

        workManager.cancelAllWork()
        clearDatabase()
        emit(false)

    }
    .flowOn(coroutineDispatcherProvider.IO())

    private suspend fun clearDatabase() {

        postsRepository.deleteAllPosts()
        feedsRepository.deleteAllFeeds()
    }
}