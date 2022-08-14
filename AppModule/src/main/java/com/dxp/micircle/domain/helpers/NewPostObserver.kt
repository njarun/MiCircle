package com.dxp.micircle.domain.helpers

import com.dxp.micircle.domain.router.model.PostModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class NewPostObserver @Inject constructor() {

    private val subject = PublishSubject.create<PostModel>()

    fun publish(value: PostModel) {
        subject.onNext(value)
    }

    fun getObservable(): Observable<PostModel> {
        return subject.hide()
    }
}