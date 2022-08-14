package com.dxp.micircle.presentation.dashboard.pages.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dxp.micircle.R
import com.dxp.micircle.domain.helpers.AppSchedulers
import com.dxp.micircle.domain.router.model.FeedMediaModel
import com.dxp.micircle.domain.router.model.FeedModel
import com.dxp.micircle.domain.router.model.UserModel
import com.dxp.micircle.domain.usecase.FirebaseFetchUser
import com.dxp.micircle.domain.usecase.FirebaseGetFeeds
import com.dxp.micircle.domain.usecase.FirebaseUserLogout
import com.dxp.micircle.presentation.base.*
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import com.dxp.micircle.presentation.dashboard.pages.home.FeedListener
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel @Suppress("UNCHECKED_CAST")
class ProfileViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth,
   private val userLogout: FirebaseUserLogout, private val feeds: FirebaseGetFeeds,
   private val fetchUser: FirebaseFetchUser, private var schedulers: AppSchedulers): BaseViewModel(), FeedListener {

    private val _userModelLive = MutableLiveData<UserModel>()
    val userModelLive: LiveData<UserModel> = _userModelLive

    private val _feedListLive = MutableLiveData<ArrayList<FeedModel>>(ArrayList())
    val feedListLive: LiveData<ArrayList<FeedModel>> = _feedListLive

    init {

        fetchProfile()

        watchNewPost()

        fetchData(-1)
    }

    fun getCurrentUserId() :String? {
        return firebaseAuth.currentUser?.uid
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

    private fun fetchProfile() {

        firebaseAuth.currentUser?.let {

            subscription {

                fetchUser.execute(it.uid)
                    .subscribeOn(schedulers.ioScheduler)
                    .observeOn(schedulers.uiScheduler)
                    .subscribe({ userModel ->

                        _userModelLive.value = userModel
                    }, {

                        emitAction(OnException(it))
                    })
            }
        }
    }

    private fun fetchData(from: Long) {

        try {

            if(viewRefreshState.value == true) {
                return
            }

            _viewRefreshState.postValue(true)

            subscription {

                feeds.getAllFeeds(from)
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

        fetchProfile()

        fetchData(-1)
    }

    fun logout() {

        try {

            if(viewRefreshState.value == true) {

                emitAction(ShowToast(R.string.please_wait))
            }
            else {

                _viewRefreshState.postValue(true)

                subscription {

                    userLogout()
                        .subscribeOn(schedulers.ioScheduler)
                        .observeOn(schedulers.uiScheduler)
                        .subscribe({ result ->

                            _viewRefreshState.postValue(false)

                            if(result) {

                                clearAllUserDataAndRestart()
                            }
                            else emitAction(OnFailed)
                        },{

                            _viewRefreshState.postValue(false)
                            emitAction(OnException(it))
                        })
                }
            }
        }
        catch (e: Exception) {

            _viewRefreshState.postValue(false)
            emitAction(OnException(e))
        }
    }

    private fun clearAllUserDataAndRestart() {

        viewModelScope.launch {

            userLogout.clearUserData().onStart {

                _viewRefreshState.postValue(true)
            }
                .catch {

                    _viewRefreshState.postValue(false)
                    emitAction(OnLogout)
                }
                .collect {

                    emitAction(OnLogout)
                }
        }
    }

    fun editProfile() {

        emitAction(ShowToast(R.string.out_of_scope_functionalities))
    }

    private fun deleteFeed(feedModel: FeedModel) {

        if(viewRefreshState.value == true) {

            emitAction(ShowToast(R.string.please_wait))
            return
        }

        viewModelScope.launch {

            try {

                feeds.deleteFeed(feedModel).onStart {

                    _viewRefreshState.postValue(true)
                }
                    .catch {

                        _viewRefreshState.postValue(false)
                        emitAction(OnException(it))
                    }
                    .collect {

                        _viewRefreshState.postValue(!it)

                        if(it) {

                            _feedListLive.value?.let {

                                val feedList: ArrayList<FeedModel> = it
                                feedList.remove(feedModel)

                                _feedListLive.value = feedList
                            }
                        }
                    }
            }
            catch (e: Exception) {

                e.printStackTrace()

                _viewRefreshState.postValue(false)

                emitAction(OnException(e))
            }
        }
    }

    override fun onCleared() {

        super.onCleared()
        feeds.onCleared()
    }

    override fun onPostSelected(postPos: Int, postObj: BaseListItem) {

        Timber.d("Selected post ${postPos} ${(postObj as FeedModel).userName}")
    }

    override fun onPostLike(postPos: Int, postObj: BaseListItem) {

        Timber.d("Like post ${postPos} ${(postObj as FeedModel).userName}")

        emitAction(ShowToast(R.string.out_of_scope_functionalities))
    }

    override fun onPostComment(postPos: Int, postObj: BaseListItem) {

        Timber.d("Comment post ${postPos} ${(postObj as FeedModel).userName}")

        emitAction(ShowToast(R.string.out_of_scope_functionalities))
    }

    override fun onPostDelete(postPos: Int, postObj: BaseListItem) {

        Timber.d("Delete post ${postPos} ${(postObj as FeedModel).userName}")

        deleteFeed(postObj)
    }

    override fun onMediaSelected(mediaPos: Int, mediaObj: BaseListItem, postPos: Int, postObj: BaseListItem) {

        Timber.d("onMediaSelected $mediaPos ${(mediaObj as FeedMediaModel).url} $mediaPos ${(postObj as FeedModel).userName}")

        emitAction(OpenMediaViewer(mediaObj.url))
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