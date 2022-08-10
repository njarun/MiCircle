package com.dxp.micircle.presentation.splash

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.databinding.ActivitySplashBinding
import com.dxp.micircle.presentation.base.BaseActivity

@SuppressLint("CustomSplashScreen") //Customizations are limited - let it evolve! -- nj
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        setWindowFlags()
        super.onCreate(savedInstanceState)
    }

    override fun onCreated(viewBinding: ViewBinding) {

        viewModel.onInit()

        getViewBinding().apply {

            lifecycleOwner = this@SplashActivity
            viewModel = this@SplashActivity.viewModel
        }
    }

    private fun setWindowFlags() {

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        requestWindowFeature(Window.FEATURE_NO_TITLE)//will hide the title
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
}