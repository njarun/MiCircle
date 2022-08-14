package com.dxp.micircle.presentation.dashboard.pages.home

import androidx.lifecycle.MutableLiveData
import com.dxp.micircle.domain.router.model.FeedModel
import com.dxp.micircle.domain.usecase.FirebaseGetFeeds
import com.dxp.micircle.presentation.base.BaseViewModel
import com.dxp.micircle.presentation.base.OnException
import com.dxp.micircle.presentation.base.OnNewPost
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val feeds: FirebaseGetFeeds) : BaseViewModel(), FeedListener {

    private val _feedListLive = MutableLiveData<ArrayList<FeedModel>>(ArrayList())
    val feedListLive: MutableLiveData<ArrayList<FeedModel>> = _feedListLive

    fun createNewPost() {
        emitAction(OnNewPost)
    }

    init {

        fetchData(-1)
    }

    private fun fetchData(from: Long) {

        try {

            _viewRefreshState.postValue(true)

            subscription {

                feeds(from)
                    .subscribeOn(feeds.schedulers.ioScheduler)
                    .observeOn(feeds.schedulers.uiScheduler)
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