package com.dxp.micircle.presentation.dashboard.pages.profile

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.R
import com.dxp.micircle.databinding.FragmentProfileBinding
import com.dxp.micircle.presentation.base.*
import com.dxp.micircle.presentation.dashboard.pages.home.FeedListAdapter
import com.dxp.micircle.presentation.image_viewer.ImageViewerActivity
import com.dxp.micircle.presentation.startup.splash.SplashActivity
import com.dxp.micircle.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val viewModel: ProfileViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = FragmentProfileBinding.inflate(layoutInflater)

    override fun onCreated(viewBinding: ViewBinding) {

        getViewBinding().apply {

            lifecycleOwner = this@ProfileFragment
            selfId = this@ProfileFragment.viewModel.getCurrentUserId()
            viewModel = this@ProfileFragment.viewModel
            adapter = FeedListAdapter(listOf(), this@ProfileFragment.viewModel.getCurrentUserId(), this@ProfileFragment.viewModel)
        }
    }

    override fun handleVMInteractions(interaction: Interactor): Boolean {

        when(interaction) {

            is OnLogout -> {

                return super.handleVMInteractions(FinishAndOpenNextScreen(SplashActivity::class.java, true))
            }

            is OnFailed -> {

                return super.handleVMInteractions(ShowToast(R.string.logout_failed))
            }

            is OpenMediaViewer -> {

                val bundle = Bundle()
                bundle.putString(Constants.EXTRA_IMAGE_PATH, interaction.mediaPath)
                return super.handleVMInteractions(OpenNextScreen(ImageViewerActivity::class.java, bundle))
            }
        }

        return super.handleVMInteractions(interaction)
    }
}