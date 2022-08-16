package com.dxp.micircle.domain.router.repository

import com.dxp.micircle.data.dto.model.FeedModel
import com.dxp.micircle.domain.router.model.PostModel
import io.reactivex.Single

interface FeedsRepository {

    fun getFeedsFromNetwork(from: Long, uid: String?): Single<ArrayList<FeedModel>>

    fun getFeedsFromLocal(uid: String?): Single<ArrayList<FeedModel>>

    suspend fun saveFeeds(feedModelList: List<FeedModel>)

    fun processNewPostToFeed(newPost: PostModel): FeedModel

    suspend fun deleteFeed(feedModel: FeedModel): Boolean

    suspend fun deleteAllFeeds()
}