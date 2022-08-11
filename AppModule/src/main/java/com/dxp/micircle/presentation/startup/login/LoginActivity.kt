package com.dxp.micircle.presentation.startup.login

import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.R
import com.dxp.micircle.databinding.ActivityLoginBinding
import com.dxp.micircle.presentation.base.*
import com.dxp.micircle.presentation.home.MainActivity
import com.dxp.micircle.presentation.startup.registration.RegistrationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

    override fun handleVMInteractions(interaction: Interactor): Boolean {

        when(interaction) {

            is OnSuccess -> {

                return super.handleVMInteractions(FinishAndOpenNextScreen(MainActivity::class.java, false))
            }

            is OnFailed -> {

                return super.handleVMInteractions(ShowToast(getString(R.string.login_failed)))
            }

            is OnNewAccount -> {

                return super.handleVMInteractions(OpenNextScreen(RegistrationActivity::class.java))
            }
        }

        return super.handleVMInteractions(interaction)
    }
}