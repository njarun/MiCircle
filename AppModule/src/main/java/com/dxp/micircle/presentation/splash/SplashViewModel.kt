package com.dxp.micircle.presentation.splash

import com.dxp.micircle.presentation.base.AppInterface
import com.dxp.micircle.presentation.base.BaseViewModel
import com.dxp.micircle.presentation.base.OpenNextScreen
import com.dxp.micircle.presentation.home.MainActivity

class SplashViewModel : BaseViewModel() {

    private var transitionIsReady = false
    private var userValidationIsReady = false

    val listener = object : AppInterface {

        override fun onCallback(vararg obj: Any) {

            transitionIsReady = true
            openNextPageWhenReady()
        }
    }

    fun onInit() {

        userValidationIsReady = true

        openNextPageWhenReady()
    }

    private fun openNextPageWhenReady() {

        if(transitionIsReady && userValidationIsReady) {

            val clazz = if (true) MainActivity::class.java else MainActivity::class.java
            emitAction(OpenNextScreen(clazz))
        }
    }
}