package com.dxp.micircle.domain.router.model

import android.os.Parcelable
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val userId: String,
    val created: Long,
    val fName: String,
    val lNAme: String,
    val profileImageUrl: String?
) : Parcelable, BaseListItem