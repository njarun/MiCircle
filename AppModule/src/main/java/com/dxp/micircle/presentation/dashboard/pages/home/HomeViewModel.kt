package com.dxp.micircle.presentation.dashboard.pages.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dxp.micircle.presentation.base.BaseViewModel
import com.dxp.micircle.presentation.base.OnNewPost

class HomeViewModel : BaseViewModel() {

    private val _viewRefreshState = MutableLiveData(false)
    val viewRefreshState: LiveData<Boolean> = _viewRefreshState

    fun createNewPost() {
        emitAction(OnNewPost)
    }
}