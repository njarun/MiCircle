package com.dxp.micircle.utils

import com.bumptech.glide.load.HttpException
import com.dxp.micircle.R

object ExceptionParser {

    fun getMessage(exception: Exception): Int {
        return when (exception) {
            is HttpException -> getHttpErrorMessage(exception)
            else -> generalError()
        }
    }

    private fun getHttpErrorMessage(exception: Exception): Int {
        return generalError()
    }

    private fun generalError() = R.string.error_general
}