package com.dxp.micircle.domain.usecase

import com.dxp.micircle.data.database.dao.FeedDao
import com.dxp.micircle.data.router.CoroutineDispatcherProvider
import com.dxp.micircle.domain.helpers.AppSchedulers
import com.dxp.micircle.domain.helpers.NewPostObserver
import com.dxp.micircle.domain.router.model.FeedModel
import com.dxp.micircle.domain.router.repository.FeedsRepository
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
    lateinit var newPostObserver: NewPostObserver

    @Inject
    lateinit var feedsDao: FeedDao

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

    fun getAllFeeds(from: Long, uid: String? = null, saveToLocal: Boolean = false) : Observable<ArrayList<FeedModel>> {

        val subject = PublishSubject.create<ArrayList<FeedModel>>()
        return subject.doOnSubscribe {

            if(from == -1L) {

                subscriptions.add(feedsRepository.getFeedsFromLocal(uid)
                    .subscribeOn(schedulers.ioScheduler)
                    .observeOn(schedulers.uiScheduler)
                    .subscribe({ result ->

                        if(result.isNotEmpty())
                            subject.onNext(result)
                    }, {

                        subject.onError(it)
                    }))
            }

            subscriptions.add(feedsRepository.getFeedsFromNetwork(from, uid)
                .subscribeOn(schedulers.ioScheduler)
                .observeOn(schedulers.uiScheduler)
                .subscribe({ result ->

                    if(from == -1L && saveToLocal && result.isNotEmpty())
                        deleteAndSaveAllFeeds(result)

                    subject.onNext(result)
                }, {

                    subject.onError(it)
                }))
        }
    }

    private fun deleteAndSaveAllFeeds(newFeedList: ArrayList<FeedModel>) {

        CoroutineScope(coroutineDispatcherProvider.IO()).launch {

            feedsRepository.deleteAllFeeds()
            feedsRepository.saveFeeds(newFeedList)
        }
    }

    fun deleteFeed(feedModel: FeedModel) = flow {

        emit(feedsRepository.deleteFeed(feedModel))
    }
    .flowOn(coroutineDispatcherProvider.IO())

    fun onCleared() {

        subscriptions.dispose()
    }
}