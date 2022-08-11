package com.dxp.micircle.presentation.startup.login

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.R
import com.dxp.micircle.databinding.ActivityLoginBinding
import com.dxp.micircle.presentation.base.*
import com.dxp.micircle.presentation.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint @SuppressLint("CustomSplashScreen") //Customizations are limited - let it evolve! -- nj
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreated(viewBinding: ViewBinding) {

        viewModel.onInit()

        getViewBinding().apply {

            lifecycleOwner = this@LoginActivity
            viewModel = this@LoginActivity.viewModel
        }
    }

    override fun handleVMInteractions(command: Interactor): Boolean {

        when(command) {

            is OnSuccess -> {

                return super.handleVMInteractions(OpenNextScreen(MainActivity::class.java))
            }

            is OnFailed -> {

                return super.handleVMInteractions(ShowToast(getString(R.string.login_failed)))
            }
        }

        return super.handleVMInteractions(command)
    }
}