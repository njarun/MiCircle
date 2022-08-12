package com.dxp.micircle.presentation.startup.registration

import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.R
import com.dxp.micircle.databinding.ActivityRegistrationBinding
import com.dxp.micircle.presentation.base.*
import com.dxp.micircle.presentation.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : BaseActivity<ActivityRegistrationBinding, RegistrationViewModel>() {

    override val viewModel: RegistrationViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = ActivityRegistrationBinding.inflate(layoutInflater)

    override fun onCreated(viewBinding: ViewBinding) {

        getViewBinding().apply {

            lifecycleOwner = this@RegistrationActivity
            viewModel = this@RegistrationActivity.viewModel
        }
    }

    override fun handleVMInteractions(interaction: Interactor): Boolean {

        when(interaction) {

            is OnSuccess -> {

                return super.handleVMInteractions(FinishAndOpenNextScreen(DashboardActivity::class.java, true))
            }

            is OnFailed -> {

                return super.handleVMInteractions(ShowToast(getString(R.string.registration_failed)))
            }
        }

        return super.handleVMInteractions(interaction)
    }
}