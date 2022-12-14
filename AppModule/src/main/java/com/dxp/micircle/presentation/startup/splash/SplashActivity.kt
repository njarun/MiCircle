package com.dxp.micircle.presentation.startup.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.databinding.ActivitySplashBinding
import com.dxp.micircle.presentation.base.*
import com.dxp.micircle.presentation.dashboard.DashboardActivity
import com.dxp.micircle.presentation.startup.login.LoginActivity
import com.dxp.micircle.utils.Utility.makeActivityFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint @SuppressLint("CustomSplashScreen") //Customizations are limited - let it evolve! -- nj
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {

    override val viewModel: SplashViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = ActivitySplashBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {

        makeActivityFullScreen(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreated(viewBinding: ViewBinding) {

        viewModel.postInit()

        getViewBinding().apply {

            lifecycleOwner = this@SplashActivity
            viewModel = this@SplashActivity.viewModel
        }
    }

    override fun handleVMInteractions(interaction: Interactor): Boolean {

        when(interaction) {

            is OnSuccess -> {

                return super.handleVMInteractions(OpenNextScreen(DashboardActivity::class.java))
            }

            is OnFailed -> {

                return super.handleVMInteractions(OpenNextScreen(LoginActivity::class.java))
            }
        }

        return super.handleVMInteractions(interaction)
    }
}