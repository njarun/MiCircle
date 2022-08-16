package com.dxp.micircle.data.dto.model

import android.os.Parcel
import android.os.Parcelable
import com.dxp.micircle.presentation.base.adapters.BaseListItem
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

@Parcelize
class UserModel(): BaseListItem, Parcelable {

    constructor(userId: String, created: Long, fName: String, lName: String, profileImageUrl: String?) : this() {

        this.userId = userId
        this.created = created
        this.fName = fName
        this.lName = lName
        this.profileImageUrl = profileImageUrl
    }

    var userId: String? = null
    var created: Long? = null
    var fName: String? = null
    var lName: String? = null
    var profileImageUrl: String? = null

    private companion object : Parceler<UserModel> {

        override fun UserModel.write(parcel: Parcel, flags: Int) {

            parcel.writeString(userId)
            parcel.writeLong(created ?: -1)
            parcel.writeString(fName)
            parcel.writeString(lName)
            parcel.writeString(profileImageUrl)
        }

        override fun create(parcel: Parcel): UserModel {

            val userModel = UserModel()
            userModel.userId = parcel.readString()
            userModel.created = parcel.readLong()
            userModel.fName = parcel.readString()
            userModel.lName = parcel.readString()
            userModel.profileImageUrl = parcel.readString()

            return userModel
        }
    }
}