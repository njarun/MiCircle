package com.dxp.micircle.presentation.new_post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dxp.micircle.Config
import com.dxp.micircle.R
import com.dxp.micircle.data.entities.PrivacyType
import com.dxp.micircle.domain.dto.model.MediaModel
import com.dxp.micircle.domain.dto.model.PostModel
import com.dxp.micircle.domain.usecase.FirebasePostToBackend
import com.dxp.micircle.domain.usecase.MapMediaToModel
import com.dxp.micircle.presentation.base.*
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import com.dxp.micircle.presentation.base.adapters.ItemListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
class NewPostViewModel @Inject constructor(private val mapper: MapMediaToModel, private val postData: FirebasePostToBackend): BaseViewModel(), ItemListener {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase

    @Inject
    lateinit var firebaseStorage: FirebaseStorage

    private val _mediaModelsLive = MutableLiveData<ArrayList<MediaModel>>(ArrayList())
    val mediaModelsLive: MutableLiveData<ArrayList<MediaModel>> = _mediaModelsLive

    private lateinit var apiJob: Job

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

    fun postToFirebase(text: String?) {

        Timber.d("postToFirebase")

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

                val postModel = PostModel(
                    postId,
                    it.uid,
                    System.currentTimeMillis(),
                    text,
                    PrivacyType.PUBLIC.value,
                    mediaModelsLive.value)

                apiJob = viewModelScope.launch {

                    var postRef: DatabaseReference? = null
                    var postMediaRef: StorageReference? = null

                    try {

                        postRef = firebaseDatabase.reference.child(Config.FBD_POSTS_PATH)
                            .child(postId)

                        postMediaRef = firebaseStorage.reference
                            .child(postModel.userId)
                            .child(Config.FBS_POSTS_MEDIA_PATH)
                            .child(postId)

                        postData.execute(postModel, postRef, postMediaRef).onStart {

                            _viewRefreshState.postValue(true)
                        }
                        .catch {

                            _viewRefreshState.postValue(false)

                            emitAction(if(it.message?.equals("-1") == true) ShowToast(R.string.post_failed_try_again) else OnException(it))

                            postRef.removeValue()
                            postMediaRef.delete()
                        }
                        .collect {

                            _viewRefreshState.postValue(false)
                            emitAction(ShowToast(R.string.posted_to_timeline_success))
                            emitAction(CloseScreen)
                        }
                    }
                    catch (e: Exception) {

                        e.printStackTrace()

                        _viewRefreshState.postValue(false)

                        emitAction(OnException(e))

                        postRef?.removeValue()
                        postMediaRef?.delete()
                    }
                }
            }
        }
    }

    override fun onItemSelected(position: Int, itemObj: BaseListItem) { //From Media Preview List
        emitAction(ShowToast(R.string.out_of_scope_functionalities))
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