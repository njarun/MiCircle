package com.dxp.micircle.domain.usecase

import com.dxp.micircle.domain.helpers.AppSchedulers
import com.dxp.micircle.domain.router.repository.FeedsRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FirebaseGetFeeds @Inject constructor(var schedulers: AppSchedulers, private val feedsRepository: FeedsRepository) {

    private val subscriptions = CompositeDisposable()

    operator fun invoke(from: Long, to: Long) = feedsRepository.getFeeds(from, to)

    fun onCleared() {

        subscriptions.dispose()
    }
}