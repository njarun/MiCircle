package com.dxp.micircle.domain.dto.model

import android.os.Parcelable
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaModel(
    var mediaId: String,
    var postId: String?,
    var type: Int,
    var url: String,
    var size: Long,
) : Parcelable, BaseListItem