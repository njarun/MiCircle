package com.dxp.micircle.domain.usecase

import com.dxp.micircle.data.router.CoroutineDispatcherProvider
import com.dxp.micircle.domain.helpers.AppSchedulers
import com.dxp.micircle.domain.helpers.NewPostObserver
import com.dxp.micircle.domain.router.model.FeedModel
import com.dxp.micircle.domain.router.repository.FeedsRepository
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class FirebaseGetFeeds @Inject constructor(var schedulers: AppSchedulers,
   private val feedsRepository: FeedsRepository, private val coroutineDispatcherProvider: CoroutineDispatcherProvider) {

    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase

    @Inject
    lateinit var newPostObserver: NewPostObserver

    private val subscriptions = CompositeDisposable()

    fun watchNewPost() : Observable<FeedModel> {

        val subject = PublishSubject.create<FeedModel>()
        return subject.doOnSubscribe {

            subscriptions.add(newPostObserver.getObservable().subscribe {

                CoroutineScope(coroutineDispatcherProvider.IO()).launch {

                    val feedModel = feedsRepository.processNewPostToFeed(it)
                    subject.onNext(feedModel)
                }
            })
        }
    }

    fun getAllFeeds(from: Long) = feedsRepository.getFeeds(from)

    fun deleteFeed(feedModel: FeedModel) = flow {

        emit(feedsRepository.deleteFeed(feedModel))
    }
    .flowOn(coroutineDispatcherProvider.IO())

    fun onCleared() {

        subscriptions.dispose()
    }
}