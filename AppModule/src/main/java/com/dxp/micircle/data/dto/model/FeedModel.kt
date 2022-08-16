package com.dxp.micircle.data.dto.model

import com.dxp.micircle.presentation.base.adapters.BaseListItem

class FeedModel() : BaseListItem {

    constructor(postId: String, userId: String, timestamp: Long, text: String?, postPrivacy: Int, mediaList: ArrayList<FeedMediaModel>?, userName: String?, imageUrl: String?) : this() {

        this.postId = postId
        this.userId = userId
        this.timestamp = timestamp
        this.text = text
        this.postPrivacy = postPrivacy
        this.mediaList = mediaList
        this.userName = userName
        this.imageUrl = imageUrl
    }

    var postId: String = ""
    var userId: String = ""
    var timestamp: Long = 0
    var text: String? = null
    var postPrivacy: Int = 0
    var mediaList: ArrayList<FeedMediaModel>? = null
    var userName: String? = null
    var imageUrl: String?  = null
}