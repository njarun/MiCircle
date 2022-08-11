package com.dxp.micircle.presentation.dashboard.pages.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dxp.micircle.domain.usecase.FirebaseUserLogout
import com.dxp.micircle.helpers.AppSchedulers
import com.dxp.micircle.presentation.base.BaseViewModel
import com.dxp.micircle.presentation.base.OnException
import com.dxp.micircle.presentation.base.OnFailed
import com.dxp.micircle.presentation.base.OnLogout
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userLogout: FirebaseUserLogout): BaseViewModel() {

    @Inject
    lateinit var schedulers: AppSchedulers

    private val _viewRefreshState = MutableLiveData(false)
    val viewRefreshState: LiveData<Boolean> = _viewRefreshState

    fun logout() {

        try {

            _viewRefreshState.postValue(true)

            subscription {

                userLogout()
                    .subscribeOn(schedulers.ioScheduler)
                    .observeOn(schedulers.uiScheduler)
                    .subscribe({ result ->

                        _viewRefreshState.postValue(false)
                        emitAction(if (result) OnLogout else OnFailed)
                    },{

                        _viewRefreshState.postValue(false)
                        emitAction(OnException(it))
                    })
            }
        }
        catch (e: Exception) {

            _viewRefreshState.postValue(false)
            emitAction(OnException(e))
        }
    }
}