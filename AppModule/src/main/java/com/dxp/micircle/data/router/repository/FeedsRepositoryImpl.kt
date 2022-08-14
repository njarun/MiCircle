package com.dxp.micircle.data.router.repository

import com.dxp.micircle.Config
import com.dxp.micircle.data.dto.mapper.PostsModelFeedMapper.toFeedModel
import com.dxp.micircle.domain.router.model.FeedModel
import com.dxp.micircle.domain.router.model.PostModel
import com.dxp.micircle.domain.router.model.UserModel
import com.dxp.micircle.domain.router.repository.FeedsRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class FeedsRepositoryImpl @Inject constructor(private val FirebaseFirestore: FirebaseFirestore) : FeedsRepository {

    override fun getFeeds(from: Long, uid: String?): Single<ArrayList<FeedModel>> {

        val subject = SingleSubject.create<ArrayList<FeedModel>>()
        return subject.doOnSubscribe {

            val startAfter = if(from == -1L)
                System.currentTimeMillis()
            else from

            val postRef = FirebaseFirestore.collection(Config.FBD_POSTS_PATH)
            val userRef = FirebaseFirestore.collection(Config.FBD_USERS_PATH)

            val query: Query //@Todo combine query. When combined the result order was chaos, check this -- nj

            if(uid == null) {

                query = postRef.orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(startAfter.toDouble())
                    .limit(Config.FEED_PAGE_LIMIT)
            }
            else {

                query = postRef.whereEqualTo("userId", uid).orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(startAfter.toDouble())
                    .limit(Config.FEED_PAGE_LIMIT)
            }

            val task = query.get()

            Tasks.await(task)

            val userMap: HashMap<String, Pair<String, String?>> = HashMap()

            var feedList: ArrayList<FeedModel> = ArrayList()

            if(task.isSuccessful) {

                feedList = task.result.toObjects(FeedModel::class.java) as ArrayList<FeedModel>

                feedList.forEach { feed ->

                    var nameAndUrlPair: Pair<String, String?>? = userMap[feed.userId!!]

                    if(nameAndUrlPair == null) {

                        val userTask = userRef.document(feed.userId!!).get()
                        Tasks.await(userTask)
                        if (userTask.isSuccessful) {
                            val user: UserModel = userTask.result.toObject(UserModel::class.java)!!
                            nameAndUrlPair = Pair("${user.fName} ${user.lName}", user.profileImageUrl)
                            userMap.put(feed.userId!!, nameAndUrlPair)
                        }
                    }

                    feed.userName = nameAndUrlPair?.first
                    feed.imageUrl = nameAndUrlPair?.second
                }
            }

            subject.onSuccess(feedList)
        }
    }

    override suspend fun processNewPostToFeed(newPost: PostModel): FeedModel {

        val feedModel = newPost.toFeedModel()
        val userRef = FirebaseFirestore.collection(Config.FBD_USERS_PATH)
        val userTask = userRef.document(feedModel.userId!!).get()
        Tasks.await(userTask)

        if (userTask.isSuccessful) {

            val user: UserModel = userTask.result.toObject(UserModel::class.java)!!
            feedModel.userName = "${user.fName} ${user.lName}"
            feedModel.imageUrl = user.profileImageUrl
        }
        else throw Exception("-1")

        return feedModel
    }

    override suspend fun deleteFeed(feedModel: FeedModel): Boolean {

        val postRef = FirebaseFirestore.collection(Config.FBD_POSTS_PATH)
        val task = postRef.document(feedModel.postId!!).delete()
        Tasks.await(task)
        return true
    }
}