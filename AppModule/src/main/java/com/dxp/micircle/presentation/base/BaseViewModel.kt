package com.dxp.micircle.presentation.base

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

open class BaseViewModel : ViewModel() {

    private val subscriptions = CompositeDisposable()

    private val interactionSubject: Subject<Interactor> = PublishSubject.create()
    val interactions: Observable<Interactor> = interactionSubject.hide()

    protected fun emitAction(command: Interactor) {
        interactionSubject.onNext(command)
    }

    protected fun subscription(block: () -> Disposable) {
        subscriptions.add(block())
    }

    fun onBackPressed() {

        emitAction(OnBackPressed)
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}