package com.dxp.micircle.presentation.home

import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.databinding.ActivityHomeBinding
import com.dxp.micircle.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = ActivityHomeBinding.inflate(layoutInflater)

    override fun onCreated(viewBinding: ViewBinding) {


    }
}