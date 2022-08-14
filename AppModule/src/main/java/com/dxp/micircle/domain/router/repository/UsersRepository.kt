package com.dxp.micircle.domain.router.repository

import com.dxp.micircle.domain.router.model.UserModel
import io.reactivex.Single

interface UsersRepository {

    fun getUser(uid: String): Single<UserModel>
}