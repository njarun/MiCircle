package com.dxp.micircle.data.router.repository

import com.dxp.micircle.Config
import com.dxp.micircle.data.dto.mapper.PostsModelFeedMapper.toFeedModel
import com.dxp.micircle.domain.router.model.FeedModel
import com.dxp.micircle.domain.router.model.PostModel
import com.dxp.micircle.domain.router.model.UserModel
import com.dxp.micircle.domain.router.repository.FeedsRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject
import kotlin.math.absoluteValue

@Suppress("BlockingMethodInNonBlockingContext")
class FeedsRepositoryImpl @Inject constructor(private val firebaseDatabase: FirebaseDatabase) : FeedsRepository {

    override fun getFeeds(from: Long, uid: String?): Single<ArrayList<FeedModel>> {

        val subject = SingleSubject.create<ArrayList<FeedModel>>()
        return subject.doOnSubscribe {

            val startAfter = if(from == -1L)
                System.currentTimeMillis()
            else from

            val postRef = firebaseDatabase.reference.child(Config.FBD_POSTS_PATH)
            val userRef = firebaseDatabase.reference.child(Config.FBD_USERS_PATH)

            val query = postRef.orderByChild("timestamp")
                .startAfter(-startAfter.toDouble())
                .limitToFirst(Config.FEED_PAGE_LIMIT)

            val task = query.get()

            Tasks.await(task)

            val feedList: ArrayList<FeedModel> = ArrayList()
            val userMap: HashMap<String, Pair<String, String?>> = HashMap()

            if(task.isSuccessful) {

                task.result.children.forEach { post ->

                    val feed: FeedModel = post.getValue(FeedModel::class.java)!!

                    var nameAndUrlPair: Pair<String, String?>? = userMap[feed.userId!!]

                    if(nameAndUrlPair == null) {

                        val userTask = userRef.child(feed.userId!!).get()
                        Tasks.await(userTask)
                        if (userTask.isSuccessful) {
                            val user: UserModel = userTask.result.getValue(UserModel::class.java)!!
                            nameAndUrlPair = Pair("${user.fName} ${user.lName}", user.profileImageUrl)
                            userMap.put(feed.userId!!, nameAndUrlPair)
                        }
                    }

                    feed.userName = nameAndUrlPair?.first
                    feed.imageUrl = nameAndUrlPair?.second
                    feed.timestamp = feed.timestamp.absoluteValue
                    feedList.add(feed)
                }
            }

            subject.onSuccess(feedList)
        }
    }

    override suspend fun processNewPostToFeed(newPost: PostModel): FeedModel {

        val feedModel = newPost.toFeedModel()
        feedModel.timestamp = feedModel.timestamp.absoluteValue
        val userRef = firebaseDatabase.reference.child(Config.FBD_USERS_PATH)
        val userTask = userRef.child(feedModel.userId!!).get()
        Tasks.await(userTask)

        if (userTask.isSuccessful) {

            val user: UserModel = userTask.result.getValue(UserModel::class.java)!!
            feedModel.userName = "${user.fName} ${user.lName}"
            feedModel.imageUrl = user.profileImageUrl
        }
        else throw Exception("-1")

        return feedModel
    }

    override suspend fun deleteFeed(feedModel: FeedModel): Boolean {

        val postRef = firebaseDatabase.reference.child(Config.FBD_POSTS_PATH)
        val task = postRef.child(feedModel.postId!!).removeValue()
        Tasks.await(task)
        return true
    }
}