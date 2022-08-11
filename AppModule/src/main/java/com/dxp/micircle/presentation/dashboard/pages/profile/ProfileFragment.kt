package com.dxp.micircle.presentation.dashboard.pages.profile

import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.R
import com.dxp.micircle.databinding.FragmentProfileBinding
import com.dxp.micircle.presentation.base.*
import com.dxp.micircle.presentation.startup.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = FragmentProfileBinding.inflate(layoutInflater)

    override fun onCreated(viewBinding: ViewBinding) {

        getViewBinding().apply {

            lifecycleOwner = this@ProfileFragment
            viewModel = this@ProfileFragment.viewModel
        }

        showToast(R.string.profile)
    }

    override fun handleVMInteractions(interaction: Interactor): Boolean {

        when(interaction) {

            is OnLogout -> {

                return super.handleVMInteractions(FinishAndOpenNextScreen(SplashActivity::class.java, true))
            }

            is OnFailed -> {

                return super.handleVMInteractions(ShowToast(R.string.logout_failed))
            }
        }

        return super.handleVMInteractions(interaction)
    }
}