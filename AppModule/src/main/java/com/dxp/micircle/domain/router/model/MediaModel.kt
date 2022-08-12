package com.dxp.micircle.domain.router.model

import android.os.Parcelable
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaModel(
    val mediaId: String,
    var postId: String,
    val type: Int,
    var url: String,
    val size: Long
) : Parcelable, BaseListItem