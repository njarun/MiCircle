package com.dxp.micircle.presentation.dashboard.pages.home

import com.dxp.micircle.presentation.base.BaseViewModel
import com.dxp.micircle.presentation.base.OnNewPost

class HomeViewModel : BaseViewModel() {

    fun createNewPost() {
        emitAction(OnNewPost)
    }
}