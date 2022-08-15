package com.dxp.micircle.domain.router.model

import android.os.Parcelable
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostModel(
    val postId: String,
    val userId: String,
    var timestamp: Long,
    val text: String?,
    val postPrivacy: Int,
    val mediaList: ArrayList<MediaModel>?,
    val userName: String?,
    var imageUrl: String?) : Parcelable, BaseListItem