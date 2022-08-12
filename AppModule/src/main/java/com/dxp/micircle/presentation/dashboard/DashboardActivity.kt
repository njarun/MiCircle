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

        getViewBinding().apply {

            lifecycleOwner = this@DashboardActivity
            fragmentManager = supportFragmentManager
            viewModel = this@DashboardActivity.viewModel
        }
    }
}