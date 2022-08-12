package com.dxp.micircle.presentation.new_post

import com.dxp.micircle.R
import com.dxp.micircle.databinding.ItemMediaPreviewBinding
import com.dxp.micircle.domain.router.model.MediaModel
import com.dxp.micircle.presentation.base.adapters.ItemListener
import com.dxp.micircle.presentation.base.adapters.recyclerview.BaseAdapter

class MediaPreviewListAdapter(list: List<MediaModel>, private val itemListener: ItemListener):
    BaseAdapter<ItemMediaPreviewBinding, MediaModel, ItemListener>(list, itemListener) {

    override val layoutId: Int = R.layout.item_media_preview

    override fun bind(binding: ItemMediaPreviewBinding, item: MediaModel, itemPos: Int) {

        binding.apply {

            mediaModel = item
            position = itemPos
            listener = itemListener
            executePendingBindings()
        }
    }
}