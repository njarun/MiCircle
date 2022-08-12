package com.dxp.micircle.domain.dto.model

import android.os.Parcelable
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostModel(
    var postId: String,
    var userId: String,
    var timestamp: Long,
    var text: String?,
    var postPrivacy: Int,
    var mediaList: ArrayList<MediaModel>?,
) : Parcelable, BaseListItem