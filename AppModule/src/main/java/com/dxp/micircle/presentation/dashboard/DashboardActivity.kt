package com.dxp.micircle.presentation.dashboard

import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.databinding.ActivityDashboardBinding
import com.dxp.micircle.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardActivity : BaseActivity<ActivityDashboardBinding, DashboardViewModel>() {

    override val viewModel: DashboardViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = ActivityDashboardBinding.inflate(layoutInflater)

    override fun onCreated(viewBinding: ViewBinding) {

        viewModel.onInit()

        getViewBinding().apply {

            lifecycleOwner = this@DashboardActivity
            viewModel = this@DashboardActivity.viewModel
            fragmentManager = supportFragmentManager
        }
    }
}