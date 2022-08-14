package com.dxp.micircle.presentation.dashboard.pages.home

import com.dxp.micircle.presentation.base.adapters.BaseListItem

interface FeedListener {

    fun onPostSelected(postPos: Int, postObj: BaseListItem)

    fun onPostLike(postPos: Int, postObj: BaseListItem)

    fun onPostComment(postPos: Int, postObj: BaseListItem)

    fun onPostDelete(postPos: Int, postObj: BaseListItem)

    fun onMediaSelected(mediaPos: Int, mediaObj: BaseListItem, postPos: Int, postObj: BaseListItem)

    fun onFeedScrolledToEnd(postEndPos: Int)
}