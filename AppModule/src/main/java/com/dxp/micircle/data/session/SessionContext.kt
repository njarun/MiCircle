package com.dxp.micircle.data.session

import com.dxp.micircle.data.dto.model.UserModel
import javax.inject.Inject

class SessionContext @Inject constructor() {

    var userModel: UserModel? = null
}