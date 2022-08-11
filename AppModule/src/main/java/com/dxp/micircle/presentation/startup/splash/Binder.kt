package com.dxp.micircle.presentation.startup.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.dxp.micircle.R
import com.dxp.micircle.presentation.base.AppInterface

@BindingAdapter(value = ["logo", "listener"], requireAll = false)
fun transformSplashImage(iv: ImageView, logo: Int, listener: AppInterface?) {

    iv.setImageResource(logo)
    iv.alpha = 0.4f
    iv.animate().alpha(1f).setDuration(1500).setInterpolator(AccelerateInterpolator())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                listener?.onCallback()
            }
        })
        .start()
}

@BindingAdapter("setVersionNumber")
fun TextView.setVersionNumber(prefix: String) {

    this.setTextColor(ContextCompat.getColor(context, R.color.md_grey_500))
    val versionCode: String = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    ("$prefix $versionCode").also { this.text = it }
}