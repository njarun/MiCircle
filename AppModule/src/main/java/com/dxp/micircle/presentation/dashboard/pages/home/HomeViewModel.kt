package com.dxp.micircle.presentation.dashboard.pages.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dxp.micircle.domain.helpers.AppSchedulers
import com.dxp.micircle.domain.router.model.FeedModel
import com.dxp.micircle.domain.usecase.FirebaseGetFeeds
import com.dxp.micircle.presentation.base.BaseViewModel
import com.dxp.micircle.presentation.base.OnException
import com.dxp.micircle.presentation.base.OnNewPost
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel @Suppress("UNCHECKED_CAST")
class HomeViewModel @Inject constructor(private val feeds: FirebaseGetFeeds,
            private var schedulers: AppSchedulers) : BaseViewModel(), FeedListener {

    private val _feedListLive = MutableLiveData<ArrayList<FeedModel>>(ArrayList())
    val feedListLive: LiveData<ArrayList<FeedModel>> = _feedListLive

    fun createNewPost() {
        emitAction(OnNewPost)
    }

    init {

        watchNewPost()

        fetchData(-1)
    }

    private fun watchNewPost() {

        subscription {

            feeds.watchNewPost()
                .subscribeOn(schedulers.ioScheduler)
                .observeOn(schedulers.uiScheduler)
                .subscribe({ feedModel ->

                    val feedList: ArrayList<FeedModel> = ArrayList()
                    feedList.add(feedModel)

                    _feedListLive.value = (_feedListLive.value?.let { feedList.plus(it) } ?: feedList) as ArrayList<FeedModel>
                }, {

                    it.printStackTrace()

                    emitAction(OnException(it))
                })
        }
    }

    private fun fetchData(from: Long) {

        try {

            if(viewRefreshState.value == true) {
                return
            }

            _viewRefreshState.postValue(true)

            subscription {

                feeds(from)
                    .subscribeOn(schedulers.ioScheduler)
                    .observeOn(schedulers.uiScheduler)
                    .subscribe({ result ->

                        _viewRefreshState.postValue(false)

                        if(from == -1L)
                            _feedListLive.value = result
                        else {

                            if(result.size == 1 && //To fix firebase returning same item infinitely
                                result[0].postId == feedListLive.value!![feedListLive.value!!.size - 1].postId) {
                                return@subscribe
                            }

                            _feedListLive.value = (feedListLive.value?.plus(result) ?: result) as ArrayList<FeedModel>
                        }
                    }, {

                        _viewRefreshState.postValue(false)
                        emitAction(OnException(it))
                    })
            }
        }
        catch (e: Exception) {

            _viewRefreshState.postValue(false)
            e.printStackTrace()
            emitAction(OnException(e))
        }
    }

    fun onRefresh() {

        fetchData(-1)
    }

    override fun onCleared() {

        super.onCleared()
        feeds.onCleared()
    }

    override fun onPostSelected(postPos: Int, postObj: BaseListItem) {

    }

    override fun onPostLike(postPos: Int, postObj: BaseListItem) {

    }

    override fun onPostComment(postPos: Int, postObj: BaseListItem) {

    }

    override fun onPostDelete(postPos: Int, postObj: BaseListItem) {

    }

    override fun onMediaSelected(postPos: Int, postObj: BaseListItem, mediaPos: Int, mediaObj: BaseListItem) {

    }

    override fun onFeedScrolledToEnd(postEndPos: Int) {

        var timestamp: Long = -1

        if((feedListLive.value?.size ?: 0) > 0) {

            val pos = if((feedListLive.value?.size ?: 0) > postEndPos) postEndPos else (feedListLive.value?.size ?: 1) - 1
            timestamp = feedListLive.value!![pos].timestamp
        }

        fetchData(timestamp)
    }
}