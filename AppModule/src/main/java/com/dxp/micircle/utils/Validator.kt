package com.dxp.micircle.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object Validator {

    @JvmStatic
    fun isUserNameValid(username: String) : Boolean {

        return (username.trim().isNotEmpty() &&
                !username.startsWith(" ") &&
                !username.endsWith(" ") &&
                username.trim().length > 4 &&
                username.contains("@") &&
                username.contains(".") &&
                username.trim().length < 100)
    }

    @JvmStatic
    fun isValidPassword(password: String): Boolean {

        val pattern: Pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }

    @JvmStatic
    fun isPasswordAndConfirmPasswordSame(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    @JvmStatic
    fun isTextFieldValid(value: String?) : Boolean {

        return (value != null &&
                value.trim().isNotEmpty() &&
                value.trim().length < 100)
    }
}