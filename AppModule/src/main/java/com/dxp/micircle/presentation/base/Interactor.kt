package com.dxp.micircle.presentation.base

interface Interactor

object CloseScreen: Interactor
class OpenNextScreen(val clazz: Class<*>): Interactor
class ShowToast(val message: Any): Interactor