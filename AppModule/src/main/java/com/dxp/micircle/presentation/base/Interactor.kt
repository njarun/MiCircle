package com.dxp.micircle.presentation.base

interface Interactor

class ShowToast(val message: Any): Interactor

object CloseScreen: Interactor
object OnBackPressed: Interactor
class OnException(val t: Throwable): Interactor

class OpenNextScreen(val clazz: Class<*>): Interactor
class FinishAndOpenNextScreen(val clazz: Class<*>, val finishAll: Boolean): Interactor

object OnSuccess : Interactor
object OnFailed : Interactor

object OnNewAccount : Interactor