package com.dxp.micircle.utils

import android.system.ErrnoException
import com.dxp.micircle.R
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ExceptionParser {

    fun getMessage(exception: Exception, level: Int = 1): Int {

        return when (exception) {

            is FirebaseAuthInvalidCredentialsException -> R.string.email_password_error
            is FirebaseAuthUserCollisionException -> R.string.user_already_exists
            else -> if(exception.cause != null && level == 1)
                getMessage(exception.cause as Exception, 2) else parseException(exception)
        }
    }

    private fun parseException(exception: Exception): Int {

        if (exception is FirebaseNetworkException ||
            exception is SocketTimeoutException ||
            exception is UnknownHostException ||
            exception is ConnectException ||
            exception is SocketException ||
            exception is ErrnoException) {

            return R.string.server_connection_error
        }
        else return generalError()
    }

    private fun generalError() = R.string.error_general
}