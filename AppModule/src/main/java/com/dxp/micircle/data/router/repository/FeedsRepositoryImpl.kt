package com.dxp.micircle.data.router.repository

import com.dxp.micircle.Config
import com.dxp.micircle.domain.router.model.FeedModel
import com.dxp.micircle.domain.router.model.UserModel
import com.dxp.micircle.domain.router.repository.FeedsRepository
import com.google.android.gms.tasks.Tasks
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject

class FeedsRepositoryImpl @Inject constructor(private val firebaseDatabase: FirebaseDatabase) : FeedsRepository {

    override fun getFeeds(from: Long, to: Long): Single<ArrayList<FeedModel>> {

        val subject = SingleSubject.create<ArrayList<FeedModel>>()
        return subject.doOnSubscribe {

            val postRef = firebaseDatabase.reference.child(Config.FBD_POSTS_PATH)
            val userRef = firebaseDatabase.reference.child(Config.FBD_USERS_PATH)

            val task = postRef.limitToLast(100)
                .orderByChild("timestamp")
                .get()

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

                    feedList.add(feed)
                }
            }

            subject.onSuccess(feedList)
        }
    }
}