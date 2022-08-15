package com.dxp.micircle.data.router.source

import com.dxp.micircle.domain.router.model.FeedModel
import com.dxp.micircle.domain.router.model.PostModel
import io.reactivex.Single

interface FeedsEntityData {

    fun getFeeds(from: Long, uid: String?): Single<ArrayList<FeedModel>>

    suspend fun saveFeeds(feedModelList: List<FeedModel>)

    fun processNewPostToFeed(newPost: PostModel): FeedModel

    suspend fun deleteFeed(feedModel: FeedModel): Boolean

    suspend fun deleteAllFeeds()
}