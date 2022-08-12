package com.dxp.micircle.presentation.base

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

open class BaseViewModel : ViewModel() {

    protected val _viewRefreshState = MutableLiveData(false)
    val viewRefreshState: LiveData<Boolean> = _viewRefreshState

    private val _formsErrorMld = MutableLiveData<MutableMap<String, @StringRes Int>>(mutableMapOf())
    val formsErrorLd: LiveData<MutableMap<String, Int>> = _formsErrorMld

    private val subscriptions = CompositeDisposable()

    private val interactionSubject: Subject<Interactor> = PublishSubject.create()
    val interactions: Observable<Interactor> = interactionSubject.hide()

    fun setFieldError(key: String, @StringRes value: Int) {

        _formsErrorMld.value?.apply {

            if(value == -1 && this[key] != null) {
                this.remove(key)
            }
            else if(value != -1) {
                this.put(key, value)
            }
        }
        .also {
            _formsErrorMld.value = it
        }
    }

    protected fun emitAction(command: Interactor) {
        interactionSubject.onNext(command)
    }

    protected fun subscription(block: () -> Disposable) {
        subscriptions.add(block())
    }

    fun onBackPressed() {
        emitAction(OnBackPressed)
    }

    fun postMessage(message: String) {
        emitAction(ShowToast(message))
    }

    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}