package com.dxp.micircle.domain.helpers

import com.dxp.micircle.data.dto.model.FeedModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class PostDeleteObserver @Inject constructor() {

    private val subject = PublishSubject.create<FeedModel>()

    fun publish(value: FeedModel) {
        subject.onNext(value)
    }

    fun getObservable(): Observable<FeedModel> {
        return subject.hide()
    }
}