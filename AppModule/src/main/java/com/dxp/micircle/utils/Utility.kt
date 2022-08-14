package com.dxp.micircle.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.dxp.micircle.presentation.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

object Utility {

    fun makeActivityFullScreen(activity: BaseActivity<*, *>) {

        try {

            val window = activity.window

            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

            activity.requestWindowFeature(Window.FEATURE_NO_TITLE)//will hide the title
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )

                window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            }
        }
        catch (e: Exception) {

            e.printStackTrace()
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

    @JvmStatic
    fun getDatePrettied(date: Long): String =
        SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date(date))
}