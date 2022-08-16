package com.dxp.micircle.presentation.dashboard.pages.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.databinding.FragmentHomeBinding
import com.dxp.micircle.presentation.base.*
import com.dxp.micircle.presentation.image_viewer.ImageViewerActivity
import com.dxp.micircle.presentation.new_post.NewPostActivity
import com.dxp.micircle.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onCreated(viewBinding: ViewBinding) {

        getViewBinding().apply {

            lifecycleOwner = activity
            viewModel = this@HomeFragment.viewModel
            adapter = FeedListAdapter(listOf(), this@HomeFragment.viewModel.getCurrentUserId(), this@HomeFragment.viewModel)
        }
    }

    override fun handleVMInteractions(interaction: Interactor): Boolean {

        when(interaction) {

            is OnNewPost -> {

                return super.handleVMInteractions(OpenNextScreen(NewPostActivity::class.java))
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