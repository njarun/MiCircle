package com.dxp.micircle.presentation.dashboard.pages.home

import com.dxp.micircle.R
import com.dxp.micircle.data.dto.model.FeedMediaModel
import com.dxp.micircle.data.dto.model.FeedModel
import com.dxp.micircle.databinding.ItemFeedMediaListBinding
import com.dxp.micircle.presentation.base.adapters.recyclerview.BaseAdapter

class FeedMediaListAdapter(list: List<FeedMediaModel>, private val itemListener: FeedListener):
    BaseAdapter<ItemFeedMediaListBinding, FeedMediaModel, Any>(list, itemListener) {

    override val layoutId: Int = R.layout.item_feed_media_list

    private var parentPosition = -1
    private var parentModel: FeedModel? = null

    fun setParams(feedPosition: Int, feedModel: FeedModel) {
        this.parentPosition = feedPosition
        this.parentModel = feedModel
    }

    override fun bind(binding: ItemFeedMediaListBinding, item: FeedMediaModel, itemPos: Int) {

        binding.apply {

            feedMediaModel = item
            position = itemPos
            listener = itemListener
            feedPosition = parentPosition
            feedModel = parentModel
            executePendingBindings()
        }
    }
}