package com.dxp.micircle.domain.dto.model

import android.os.Parcelable
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaModel(
    var id: Long = 0,
    var name: String? = null,
    var path: String? = null,
    var duration: Long = 0L,
    var thumb: String? = null,
    var fileSize: Long = 0L,
    var doesUri:Boolean = false
) : Parcelable, BaseListItem