package com.dxp.micircle.presentation.dashboard.pages.home

import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.databinding.FragmentHomeBinding
import com.dxp.micircle.presentation.base.BaseFragment
import com.dxp.micircle.presentation.base.Interactor
import com.dxp.micircle.presentation.base.OnNewPost
import com.dxp.micircle.presentation.base.OpenNextScreen
import com.dxp.micircle.presentation.new_post.NewPostActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun onCreated(viewBinding: ViewBinding) {

        getViewBinding().apply {

            lifecycleOwner = this@HomeFragment
            viewModel = this@HomeFragment.viewModel
        }
    }

    override fun handleVMInteractions(interaction: Interactor): Boolean {

        when(interaction) {
            is OnNewPost -> {
                return super.handleVMInteractions(OpenNextScreen(NewPostActivity::class.java))
            }
        }

        return super.handleVMInteractions(interaction)
    }
}