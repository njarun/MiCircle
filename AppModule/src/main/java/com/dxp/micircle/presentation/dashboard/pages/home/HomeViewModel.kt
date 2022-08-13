package com.dxp.micircle.presentation.dashboard.pages.home

import com.dxp.micircle.presentation.base.BaseViewModel
import com.dxp.micircle.presentation.base.OnNewPost
import com.dxp.micircle.presentation.base.adapters.BaseListItem

class HomeViewModel : BaseViewModel(), FeedListener {

    fun createNewPost() {
        emitAction(OnNewPost)
    }

    override fun onPostSelected(postPos: Int, postObj: BaseListItem) {

    }

    override fun onPostLike(postPos: Int, postObj: BaseListItem) {

    }

    override fun onPostComment(postPos: Int, postObj: BaseListItem) {

    }

    override fun onPostDelete(postPos: Int, postObj: BaseListItem) {

    }

    override fun onMediaSelected(postPos: Int, postObj: BaseListItem, mediaPos: Int, mediaObj: BaseListItem) {

    }

    override fun onFeedScrolledToEnd(postEndPos: Int) {

    }
}