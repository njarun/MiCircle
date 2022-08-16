package com.dxp.micircle.domain.usecase

import com.dxp.micircle.data.database.dao.FeedDao
import com.dxp.micircle.data.dto.mapper.PostsModelFeedMapper.toFeedModel
import com.dxp.micircle.data.dto.model.FeedModel
import com.dxp.micircle.data.router.CoroutineDispatcherProvider
import com.dxp.micircle.domain.helpers.AppSchedulers
import com.dxp.micircle.domain.helpers.NewPostObserver
import com.dxp.micircle.domain.helpers.PostDeleteObserver
import com.dxp.micircle.domain.router.repository.FeedsRepository
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class FirebaseGetFeeds @Inject constructor(val schedulers: AppSchedulers,
   private val feedsRepository: FeedsRepository, private val coroutineDispatcherProvider: CoroutineDispatcherProvider) {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var newPostObserver: NewPostObserver

    @Inject
    lateinit var postDeleteObserver: PostDeleteObserver

    @Inject
    lateinit var feedsDao: FeedDao

    private val subscriptions = CompositeDisposable()

    fun watchNewPost() : Observable<FeedModel> {

        val subject = PublishSubject.create<FeedModel>()
        return subject.doOnSubscribe {

            subscriptions.add(newPostObserver.getObservable().subscribe {

                subject.onNext(it.toFeedModel())
            })
        }
    }

    fun watchPostDelete() : Observable<FeedModel> {

        val subject = PublishSubject.create<FeedModel>()
        return subject.doOnSubscribe {

            subscriptions.add(postDeleteObserver.getObservable().subscribe {

                subject.onNext(it)
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