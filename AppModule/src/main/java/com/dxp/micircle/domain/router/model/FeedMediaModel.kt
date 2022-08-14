package com.dxp.micircle.domain.router.model

import com.dxp.micircle.presentation.base.adapters.BaseListItem

class FeedMediaModel(): BaseListItem {

    constructor(mediaId: String?, postId: String?, type: Int, url: String?, size: Long) : this() {
        this.mediaId = mediaId
        this.postId = postId
        this.type = type
        this.url = url
        this.size = size
    }

    var mediaId: String? = null
    var postId: String? = null
    var type: Int = 0
    var url: String? = null
    var size: Long = 0
}