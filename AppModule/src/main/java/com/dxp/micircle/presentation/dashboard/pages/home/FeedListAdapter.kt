package com.dxp.micircle.presentation.dashboard.pages.home

import com.dxp.micircle.R
import com.dxp.micircle.databinding.ItemFeedListBinding
import com.dxp.micircle.domain.router.model.FeedModel
import com.dxp.micircle.presentation.base.adapters.recyclerview.BaseAdapter

class FeedListAdapter(list: List<FeedModel>, private val itemListener: FeedListener):
    BaseAdapter<ItemFeedListBinding, FeedModel, Any>(list, itemListener) {

    override val layoutId: Int = R.layout.item_feed_list

    override fun bind(binding: ItemFeedListBinding, item: FeedModel, itemPos: Int) {

        binding.apply {

            feedModel = item
            position = itemPos
            listener = itemListener
            executePendingBindings()
        }
    }

    override fun onScrolledToEnd(pos: Int) {
        itemListener.onFeedScrolledToEnd(pos)
    }
}