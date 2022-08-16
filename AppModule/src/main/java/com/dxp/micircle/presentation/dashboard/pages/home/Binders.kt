package com.dxp.micircle.presentation.dashboard.pages.home

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dxp.micircle.data.dto.model.FeedMediaModel
import com.dxp.micircle.data.dto.model.FeedModel
import com.dxp.micircle.presentation.base.adapters.BaseListItem

@BindingAdapter(value = ["mediaList", "listener", "feedPosition", "feedModel"], requireAll = true) @Suppress("UNCHECKED_CAST")
fun setRecyclerAdapter(recyclerView: RecyclerView, mediaList: List<BaseListItem>?, listener: FeedListener, feedPosition: Int, feedModel: FeedModel) {

    val adapter = FeedMediaListAdapter(listOf(), listener)
    adapter.setParams(feedPosition, feedModel)
    adapter.updateData((mediaList ?: listOf()) as List<FeedMediaModel>)

    recyclerView.adapter = adapter
}