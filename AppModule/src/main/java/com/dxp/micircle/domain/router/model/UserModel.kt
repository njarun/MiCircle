package com.dxp.micircle.domain.router.model

import com.dxp.micircle.presentation.base.adapters.BaseListItem

class UserModel(): BaseListItem {

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
}