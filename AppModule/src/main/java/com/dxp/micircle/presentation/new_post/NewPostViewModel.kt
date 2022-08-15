package com.dxp.micircle.presentation.new_post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dxp.micircle.Config
import com.dxp.micircle.R
import com.dxp.micircle.domain.entities.PrivacyType
import com.dxp.micircle.domain.helpers.AppSchedulers
import com.dxp.micircle.domain.router.model.MediaModel
import com.dxp.micircle.domain.router.model.PostModel
import com.dxp.micircle.domain.router.model.UserModel
import com.dxp.micircle.domain.usecase.FirebaseFetchUser
import com.dxp.micircle.domain.usecase.FirebasePostToBackend
import com.dxp.micircle.domain.usecase.MapMediaToModel
import com.dxp.micircle.presentation.base.*
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import com.dxp.micircle.presentation.base.adapters.ItemListener
import com.google.firebase.auth.FirebaseAuth
import com.lassi.data.media.MiMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel @Suppress("UNCHECKED_CAST")
class NewPostViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth, private val mapper: MapMediaToModel, private val postData: FirebasePostToBackend,
           private val fetchUser: FirebaseFetchUser, private var schedulers: AppSchedulers): BaseViewModel(), ItemListener {

    private val _mediaModelsLive = MutableLiveData<ArrayList<MediaModel>>(ArrayList())
    val mediaModelsLive: LiveData<ArrayList<MediaModel>> = _mediaModelsLive

    private val _userModelLive = MutableLiveData<UserModel>()
    val userModelLive: LiveData<UserModel> = _userModelLive

    private lateinit var apiJob: Job

    init {

        fetchProfile() // Call this from the dash activity and pass it here instead of fetching here @Todo planned for next release -- nj
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

    fun openMediaPicker() {

        emitAction(OpenMediaPicker)
    }

    fun updateMediaSet(miMediaList: List<MiMedia>) {

        Timber.d("Selected media size - ${miMediaList.size}")

        viewModelScope.launch {

            try {

                mapper.execute(miMediaList).onStart {

                    _viewRefreshState.postValue(true)
                }
                .catch {

                    _viewRefreshState.postValue(false)
                    emitAction(OnException(it))
                }
                .collect {

                    if (it is Boolean) {

                        _viewRefreshState.postValue(it)
                    }
                    else {

                        _viewRefreshState.postValue(false)

                        _mediaModelsLive.value = (_mediaModelsLive.value?.plus(it as ArrayList<MediaModel>) ?: it as ArrayList<MediaModel>) as ArrayList<MediaModel>?

                        Timber.d("total media size - ${mediaModelsLive.value?.size}")
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

    fun postToBackend(text: String?) {

        Timber.d("postToBackend")

        if(text.isNullOrEmpty() && mediaModelsLive.value.isNullOrEmpty()) {

            emitAction(ShowToast(R.string.maybe_next_time))
            emitAction(CloseScreen)
        }
        else {

            firebaseAuth.currentUser?.let {

                val postId = UUID.randomUUID().toString()
                mediaModelsLive.value?.forEach {
                    it.postId = postId
                }

                var userName: String? = null
                var imageUrl: String? = null

                userModelLive.value?.let {
                    userName = it.fName + " " + it.lName
                    imageUrl = it.profileImageUrl
                }

                val postModel = PostModel(
                    postId,
                    it.uid,
                    System.currentTimeMillis(),
                    text?.take(Config.POST_TEXT_LIMIT),
                    PrivacyType.PUBLIC.value,
                    mediaModelsLive.value,
                    userName,
                    imageUrl)

                apiJob = viewModelScope.launch {

                    try {

                        postData.execute(postModel).onStart {

                            _viewRefreshState.postValue(true)
                        }
                        .catch {

                            _viewRefreshState.postValue(false)

                            emitAction(OnException(it))
                        }
                        .collect {

                            _viewRefreshState.postValue(false)

                            emitAction(ShowToast(if(it == 1) R.string.posted_to_timeline_success else R.string.posting_to_timeline_info))

                            emitAction(CloseScreen)
                        }
                    }
                    catch (e: Exception) {

                        e.printStackTrace()

                        _viewRefreshState.postValue(false)

                        emitAction(OnException(e))
                    }
                }
            }
        }
    }

    override fun onItemSelected(position: Int, itemObj: BaseListItem) { //From Media Preview List

        emitAction(OpenMediaViewer((itemObj as MediaModel).url))
    }

    override fun onItemRemoved(position: Int, itemObj: BaseListItem) { //From Media Preview List

        _mediaModelsLive.value = _mediaModelsLive.value?.apply {

            if(size > position)
                removeAt(position)
        }
    }

    override fun onScrolledToEnd(position: Int) { //From Media Preview List

    }

    override fun onCleared() {

        super.onCleared()

        if(::apiJob.isInitialized && apiJob.isActive) {
            apiJob.cancel()
        }
    }
}