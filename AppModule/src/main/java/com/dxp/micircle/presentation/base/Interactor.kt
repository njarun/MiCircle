package com.dxp.micircle.presentation.base

import android.content.Intent
import com.dxp.micircle.R
import com.dxp.micircle.utils.ExceptionParser
import com.dxp.micircle.utils.Utility

interface Interactor

class ShowToast(val message: Any): Interactor

object CloseScreen: Interactor
object OnBackPressed: Interactor
class OnException(val t: Throwable): Interactor

class OpenNextScreen(val clazz: Class<*>): Interactor
class FinishAndOpenNextScreen(val clazz: Class<*>, val finishAll: Boolean): Interactor

object OnSuccess : Interactor
object OnFailed : Interactor

object OnNewAccount : Interactor
object OnLogout : Interactor

fun handleVMInteractions(activity: BaseActivity<*, *>, interaction: Interactor): Boolean {

    when (interaction) {

        is OpenNextScreen -> {

            activity.startActivity(Intent(activity, interaction.clazz))
        }

        is FinishAndOpenNextScreen -> {

            if(interaction.finishAll)
                activity.finishAffinity()
            else activity.finish()

            activity.startActivity(Intent(activity, interaction.clazz))
        }

        is ShowToast -> {

            when (interaction.message) {
                is Int -> activity.showToast(interaction.message)
                is String -> activity.showToast(interaction.message)
                else -> activity.showToast(R.string.invalid_toast_msg)
            }
        }

        is OnException -> {

            var error = ExceptionParser.getMessage(interaction.t as Exception)
            if(error == R.string.server_connection_error && !Utility.isNetworkAvailable(activity))
                error = R.string.no_internet_error

            activity.showToast(error)
        }

        is OnBackPressed -> {

            activity.onBackPressed()
        }

        is CloseScreen -> {

            activity.finish()
        }
    }

    return true
}