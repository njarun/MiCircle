package com.dxp.micircle.domain.router.model

import com.dxp.micircle.presentation.base.adapters.BaseListItem

class FeedMediaModel: BaseListItem {

    var mediaId: String? = null
    var postId: String? = null
    var type: Int = 0
    var url: String? = null
    var size: Long = 0
}