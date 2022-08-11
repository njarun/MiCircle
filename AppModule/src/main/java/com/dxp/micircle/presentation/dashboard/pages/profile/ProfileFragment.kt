package com.dxp.micircle.presentation.dashboard.pages.profile

import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.R
import com.dxp.micircle.databinding.FragmentProfileBinding
import com.dxp.micircle.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = FragmentProfileBinding.inflate(layoutInflater)

    override fun onCreated(viewBinding: ViewBinding) {

        getViewBinding().apply {

            lifecycleOwner = this@ProfileFragment
            viewModel = this@ProfileFragment.viewModel
        }

        showToast(R.string.profile)
    }
}