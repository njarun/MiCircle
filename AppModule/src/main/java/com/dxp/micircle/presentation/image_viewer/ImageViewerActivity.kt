package com.dxp.micircle.presentation.image_viewer

import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.databinding.ActivityImageViewerBinding
import com.dxp.micircle.presentation.base.BaseActivity
import com.dxp.micircle.presentation.base.BaseViewModel
import com.dxp.micircle.utils.Constants
import com.dxp.micircle.utils.Utility.makeActivityFullScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageViewerActivity : BaseActivity<ActivityImageViewerBinding, BaseViewModel>() {

    override val viewModel: BaseViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = ActivityImageViewerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {

        makeActivityFullScreen(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreated(viewBinding: ViewBinding) {

        val imageUrl = intent.getStringExtra(Constants.EXTRA_IMAGE_PATH)

        if(imageUrl == null) {

            onBackPressed()
            return
        }

        getViewBinding().apply {

            lifecycleOwner = this@ImageViewerActivity
            viewModel = this@ImageViewerActivity.viewModel
            imagePath = imageUrl
        }
    }
}