package com.dxp.micircle.presentation.startup.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _userNameError = MutableLiveData("")
    val userNameError: LiveData<String> = _userNameError

    private val _passwordError = MutableLiveData("")
    val passwordError: LiveData<String> = _passwordError

    private val _viewRefreshState = MutableLiveData(false)
    val viewRefreshState: LiveData<Boolean> = _viewRefreshState

    fun onInit() {

    }

    fun onUsernameChange() {

        if(userNameError.value?.isNotEmpty() == true)
            _userNameError.value = ""
    }

    fun onPasswordChange() {

        if(passwordError.value?.isNotEmpty() == true)
            _passwordError.value = ""
    }

    fun tryLoginWithUserPass(username: String, password: String): Boolean {

        try {

            Timber.d("Try login with $username and $password")

            if(viewRefreshState.value == true) {

                emitAction(ShowToast(R.string.please_wait))
            }
            else if(!Validator.isUserNameValid(username)) {

                _userNameError.value = "Enter a valid user name"
            }
            else if(!Validator.isValidPassword(password)) {

                _passwordError.value = "Enter a valid user password"
            }
            else {

                Timber.d("Valid user pass, continue with login!")

                _viewRefreshState.postValue(true)

                subscription {

                    userLogin.invoke(username, password)
                        .subscribeOn(schedulers.ioScheduler)
                        .observeOn(schedulers.uiScheduler)
                        .subscribe({ authenticated ->

                            _viewRefreshState.postValue(false)
                            emitAction(if (authenticated) OnSuccess else OnFailed)
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

    fun postMessage(message: String) {
        emitAction(ShowToast(message))
    }
}