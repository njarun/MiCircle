package com.dxp.micircle.presentation.new_post

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import com.dxp.micircle.R
import com.dxp.micircle.databinding.ActivityNewPostBinding
import com.dxp.micircle.presentation.base.BaseActivity
import com.dxp.micircle.presentation.base.Interactor
import com.dxp.micircle.presentation.base.OpenMediaPicker
import com.lassi.common.utils.KeyUtils
import com.lassi.data.media.MiMedia
import com.lassi.domain.media.LassiOption
import com.lassi.domain.media.MediaType
import com.lassi.presentation.builder.Lassi
import com.lassi.presentation.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint @Suppress("UNCHECKED_CAST")
class NewPostActivity : BaseActivity<ActivityNewPostBinding, NewPostViewModel>() {

    override val viewModel: NewPostViewModel by viewModels()

    override fun constructViewBinding(): ViewBinding = ActivityNewPostBinding.inflate(layoutInflater)

    override fun onCreated(viewBinding: ViewBinding) {

        getViewBinding().apply {

            lifecycleOwner = this@NewPostActivity
            viewModel = this@NewPostActivity.viewModel
            adapter = MediaPreviewListAdapter(listOf(), this@NewPostActivity.viewModel)
        }
    }

    override fun handleVMInteractions(interaction: Interactor): Boolean {

        when(interaction) {

            is OpenMediaPicker -> {

                openMediaPicker()
                return true
            }
        }

        return super.handleVMInteractions(interaction)
    }

    private fun openMediaPicker() {

        val intent = Lassi(this)
            .with(LassiOption.CAMERA_AND_GALLERY)
            .setMaxCount(5)
            .setGridSize(3)
            .setMediaType(MediaType.IMAGE)
            .setCompressionRation(50)
            .setSupportedFileTypes("jpg", "jpeg", "png", "webp", "gif")
            .setStatusBarColor(R.color.window_bg_color)
            .setToolbarColor(R.color.window_bg_color)
            .setToolbarResourceColor(R.color.action_bar_item_color)
            .setProgressBarColor(R.color.colorAccent)
            .setPlaceHolder(R.drawable.ic_image_placeholder)
            .setErrorDrawable(R.drawable.ic_image_placeholder)
            .setSelectionDrawable(R.drawable.ic_checked_media)
            .setCropType(CropImageView.CropShape.RECTANGLE)
            .setCropAspectRatio(1, 1)
            .enableFlip()
            .enableRotate()
            .enableActualCircleCrop()
            .build()

        receiveData.launch(intent)
    }

    private val receiveData = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        if (it.resultCode == Activity.RESULT_OK) {

            val selectedMedia = it.data?.getSerializableExtra(KeyUtils.SELECTED_MEDIA) as List<MiMedia>
            (selectedMedia.isNotEmpty()).let { viewModel.updateMediaSet(selectedMedia) }
        }
    }
}