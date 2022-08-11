package com.dxp.micircle.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object Validator {

    fun isUserNameValid(username: String) : Boolean {

        return (username.trim().isNotEmpty() &&
                !username.startsWith(" ") &&
                !username.endsWith(" ") &&
                username.trim().length > 4 &&
                username.trim().length < 100)
    }

    fun isValidPassword(password: String): Boolean {

        val pattern: Pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }
}