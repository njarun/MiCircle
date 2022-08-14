package com.dxp.micircle.domain.router.repository

import com.dxp.micircle.domain.router.model.FeedModel
import io.reactivex.Single

interface FeedsRepository {

    fun getFeeds(from: Long): Single<ArrayList<FeedModel>>
}