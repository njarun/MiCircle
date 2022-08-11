package com.dxp.micircle.presentation.base

interface Interactor

class ShowToast(val message: Any): Interactor

object CloseScreen: Interactor
class OnException(val t: Throwable): Interactor

class OpenNextScreen(val clazz: Class<*>): Interactor
class OpenNextScreenAndFinish(val clazz: Class<*>): Interactor

object OnSuccess : Interactor
object OnFailed : Interactor