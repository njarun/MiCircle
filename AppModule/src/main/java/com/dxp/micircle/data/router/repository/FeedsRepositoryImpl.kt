package com.dxp.micircle.data.router.repository

import com.dxp.micircle.data.dto.model.FeedModel
import com.dxp.micircle.data.router.factory.FeedFactory
import com.dxp.micircle.data.router.factory.Source
import com.dxp.micircle.domain.router.model.PostModel
import com.dxp.micircle.domain.router.repository.FeedsRepository
import io.reactivex.Single
import javax.inject.Inject

class FeedsRepositoryImpl @Inject constructor(feedFactory: FeedFactory) : FeedsRepository {

    private val networkRepo = feedFactory.create(Source.NETWORK)
    private val localRepo = feedFactory.create(Source.LOCAL)

    override fun getFeedsFromLocal(uid: String?): Single<ArrayList<FeedModel>> {
        return localRepo.getFeeds(-1, uid)
    }

    override fun getFeedsFromNetwork(from: Long, uid: String?): Single<ArrayList<FeedModel>> {
        return networkRepo.getFeeds(from, uid)
    }

    override suspend fun saveFeeds(feedModelList: List<FeedModel>) {
        localRepo.saveFeeds(feedModelList)
    }

    override fun processNewPostToFeed(newPost: PostModel): FeedModel {

        localRepo.processNewPostToFeed(newPost)
        return networkRepo.processNewPostToFeed(newPost)
    }

    override suspend fun deleteFeed(feedModel: FeedModel): Boolean {

        localRepo.deleteFeed(feedModel)
        return networkRepo.deleteFeed(feedModel)
    }

    override suspend fun deleteAllFeeds() {
        localRepo.deleteAllFeeds()
    }
}