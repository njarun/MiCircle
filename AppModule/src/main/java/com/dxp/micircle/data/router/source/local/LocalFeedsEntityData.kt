package com.dxp.micircle.data.router.source.local

import com.dxp.micircle.data.database.dao.FeedDao
import com.dxp.micircle.data.database.dao.FeedMediaDao
import com.dxp.micircle.data.database.model.FeedMediaEntity
import com.dxp.micircle.data.dto.mapper.FeedsEntityMapper.toFeedModelList
import com.dxp.micircle.data.dto.mapper.FeedsModelMapper.toFeedEntity
import com.dxp.micircle.data.dto.mapper.FeedsModelMapper.toFeedEntityList
import com.dxp.micircle.data.dto.mapper.PostsModelFeedMapper.toFeedModel
import com.dxp.micircle.data.router.source.FeedsEntityData
import com.dxp.micircle.domain.router.model.FeedModel
import com.dxp.micircle.domain.router.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject

class LocalFeedsEntityData @Inject constructor(private val feedDao: FeedDao, private val feedMediaDao: FeedMediaDao, private val firebaseAuth: FirebaseAuth) : FeedsEntityData {

    override fun getFeeds(from: Long, uid: String?): Single<ArrayList<FeedModel>> {

        val subject = SingleSubject.create<ArrayList<FeedModel>>()
        return subject.doOnSubscribe {

            val feedList = uid?.let { feedDao.getFeeds(it) } ?: feedDao.getAllFeeds()
            feedList.forEach {
                it.mediaList = feedMediaDao.getAllMedia(it.postId) as ArrayList<FeedMediaEntity>
            }
            subject.onSuccess(feedList.toFeedModelList())
        }
    }

    override suspend fun saveFeeds(feedModelList: List<FeedModel>) {

        val postEntity = feedModelList.toFeedEntityList()
        feedDao.insert(postEntity)
        postEntity.forEach {
            it.mediaList?.let { mediaList -> feedMediaDao.insert(mediaList) }
        }
    }

    override fun processNewPostToFeed(newPost: PostModel): FeedModel {

        val feedModel = newPost.toFeedModel()
        val feedEntity = feedModel.toFeedEntity()

        feedDao.insert(feedEntity)
        feedEntity.mediaList?.let { feedMediaDao.insert(it) }

        return feedModel
    }

    override suspend fun deleteFeed(feedModel: FeedModel): Boolean {

        feedDao.delete(feedModel.postId)
        feedMediaDao.delete(feedModel.postId)

        return true
    }

    override suspend fun deleteAllFeeds() {

        feedDao.delete()
        feedMediaDao.delete()
    }
}