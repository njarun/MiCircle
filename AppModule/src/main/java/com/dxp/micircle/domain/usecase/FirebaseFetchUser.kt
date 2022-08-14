package com.dxp.micircle.domain.usecase

import com.dxp.micircle.domain.router.repository.UsersRepository
import javax.inject.Inject


class FirebaseFetchUser @Inject constructor(private val usersRepo: UsersRepository) {

    fun execute(uid: String) = usersRepo.getUser(uid)
}