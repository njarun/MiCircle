package com.dxp.micircle.presentation.startup.splash

import androidx.lifecycle.viewModelScope
import com.dxp.micircle.domain.usecase.FirebaseValidateUser
import com.dxp.micircle.helpers.AppSchedulers
import com.dxp.micircle.presentation.base.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val firebaseValidateUser: FirebaseValidateUser): BaseViewModel() {

    @Inject
    lateinit var schedulers: AppSchedulers

    private var transitionIsReady = false
    private var userValidationIsReady = false
    private var isAuthenticated = false

    val listener = object : AppInterface {

        override fun onCallback(vararg obj: Any) {

            transitionIsReady = true
            openNextPageWhenReady()
        }
    }

    fun onInit() {

        subscription {

            firebaseValidateUser()
                .subscribeOn(schedulers.ioScheduler)
                .observeOn(schedulers.uiScheduler)
                .subscribe({ authenticated ->

                    this.isAuthenticated = authenticated
                    userValidationIsReady = true

                    openNextPageWhenReady()
                }, {

                    emitAction(OnException(it))

                    viewModelScope.launch {

                        delay(2000)
                        emitAction(CloseScreen)
                    }
                })
        }
    }

    private fun openNextPageWhenReady() {

        if(transitionIsReady && userValidationIsReady) {

            emitAction(if(isAuthenticated) OnSuccess else OnFailed)
        }
    }
}