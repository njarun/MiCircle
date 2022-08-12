package com.dxp.micircle.domain.base

interface UseCase<T, U> {

    fun execute(param: T): U
}