package com.dxp.micircle.presentation.startup.login

import com.dxp.micircle.R
import com.dxp.micircle.domain.usecase.FirebaseUserLogin
import com.dxp.micircle.helpers.AppSchedulers
import com.dxp.micircle.presentation.base.*
import com.dxp.micircle.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userLogin: FirebaseUserLogin): BaseViewModel() {

    @Inject
    lateinit var schedulers: AppSchedulers

    fun tryLoginWithUserPass(username: String, password: String): Boolean {

        try {

            Timber.d("Try login with $username and $password")

            if(viewRefreshState.value == true) {

                emitAction(ShowToast(R.string.please_wait))
            }
            else if(!Validator.isUserNameValid(username)) {

                setFieldError("username", R.string.invalid_email)
            }
            else if(!Validator.isValidPassword(password)) {

                setFieldError("password", R.string.invalid_password_general)
            }
            else {

                _viewRefreshState.postValue(true)

                subscription {

                    userLogin.invoke(username, password)
                        .subscribeOn(schedulers.ioScheduler)
                        .observeOn(schedulers.uiScheduler)
                        .subscribe({ result ->

                            _viewRefreshState.postValue(false)
                            emitAction(if (result) OnSuccess else OnFailed)
                        }, {

                            _viewRefreshState.postValue(false)
                            emitAction(OnException(it))
                        })
                }
            }
        }
        catch (e: Exception) {

            _viewRefreshState.postValue(false)

            emitAction(OnException(e))

            e.printStackTrace()
        }

        return false
    }

    fun createNewAccount() {
        emitAction(OnNewAccount)
    }
}