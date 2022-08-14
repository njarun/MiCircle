package com.dxp.micircle.domain.router.model

import com.dxp.micircle.presentation.base.adapters.BaseListItem

class FeedModel : BaseListItem {

    var postId: String? = null
    var userId: String? = null
    var timestamp: Long = 0
    var text: String? = null
    var postPrivacy: Int = 0
    var mediaList: ArrayList<FeedMediaModel>? = null
    var userName: String? = null
    var imageUrl: String?  = null
}