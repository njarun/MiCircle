package com.dxp.micircle.presentation.new_post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dxp.micircle.domain.dto.model.MediaModel
import com.dxp.micircle.domain.usecase.MapMiMediaToMediaModel
import com.dxp.micircle.presentation.base.BaseViewModel
import com.dxp.micircle.presentation.base.OnException
import com.dxp.micircle.presentation.base.OpenMediaPicker
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import com.dxp.micircle.presentation.base.adapters.ItemListener
import com.lassi.data.media.MiMedia
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel @Suppress("UNCHECKED_CAST")
class NewPostViewModel @Inject constructor(private val mapper: MapMiMediaToMediaModel): BaseViewModel(), ItemListener {

    private val _mediaModelsLive = MutableLiveData<ArrayList<MediaModel>>(ArrayList())
    val mediaModelsLive: MutableLiveData<ArrayList<MediaModel>> = _mediaModelsLive

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

    fun postToFirebase(text: String) {

        Timber.d("postToFirebase")
    }

    override fun onItemSelected(position: Int, itemObj: BaseListItem) { //From Media Preview List


    }

    override fun onItemRemoved(position: Int, itemObj: BaseListItem) { //From Media Preview List


    }

    override fun onScrolledToEnd(position: Int) { //From Media Preview List


    }
}