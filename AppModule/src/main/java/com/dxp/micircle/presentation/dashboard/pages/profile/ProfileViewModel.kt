package com.dxp.micircle.presentation.dashboard.pages.profile

import androidx.lifecycle.viewModelScope
import com.dxp.micircle.R
import com.dxp.micircle.domain.helpers.AppSchedulers
import com.dxp.micircle.domain.usecase.FirebaseUserLogout
import com.dxp.micircle.presentation.base.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userLogout: FirebaseUserLogout): BaseViewModel() {

    @Inject
    lateinit var schedulers: AppSchedulers

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
}