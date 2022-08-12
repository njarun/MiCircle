package com.dxp.micircle.presentation.new_post

import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.databinding.ActivityNewPostBinding
import com.dxp.micircle.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPostActivity : BaseActivity<ActivityNewPostBinding, NewPostViewModel>() {

    override val viewModel: NewPostViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = ActivityNewPostBinding.inflate(layoutInflater)

    override fun onCreated(viewBinding: ViewBinding) {

        getViewBinding().apply {

            lifecycleOwner = this@NewPostActivity
            viewModel = this@NewPostActivity.viewModel
        }
    }
}