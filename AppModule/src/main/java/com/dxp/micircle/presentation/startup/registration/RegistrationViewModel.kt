package com.dxp.micircle.presentation.startup.registration

import com.dxp.micircle.R
import com.dxp.micircle.domain.helpers.AppSchedulers
import com.dxp.micircle.domain.usecase.FirebaseRegisterUser
import com.dxp.micircle.presentation.base.*
import com.dxp.micircle.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val userRegistration: FirebaseRegisterUser): BaseViewModel() {

    @Inject
    lateinit var schedulers: AppSchedulers

    fun initiateRegistration(fName: String, lName: String, username: String, password: String, confPassword: String): Boolean {

        try {

            Timber.d("Try registration with $fName, $lName, $username and $password")

            if(viewRefreshState.value == true) {

                emitAction(ShowToast(R.string.please_wait))
            }
            else if(!Validator.isTextFieldValid(fName)) {

                setFieldError("fName", R.string.invalid_f_name)
            }
            else if(!Validator.isTextFieldValid(lName)) {

                setFieldError("lName", R.string.invalid_l_name)
            }
            else if(!Validator.isUserNameValid(username)) {

                setFieldError("username", R.string.invalid_email)
            }
            else if(!Validator.isValidPassword(password)) {

                setFieldError("password", R.string.invalid_password)
            }
            else if(!Validator.isPasswordAndConfirmPasswordSame(password, confPassword)) {

                setFieldError("confPassword", R.string.invalid_conf_password)
            }
            else {

                _viewRefreshState.postValue(true)

                subscription {

                    userRegistration.invoke(fName, lName, username, password)
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
}