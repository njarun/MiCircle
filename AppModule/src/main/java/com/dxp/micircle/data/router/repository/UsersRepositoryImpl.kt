package com.dxp.micircle.data.router.repository

import com.dxp.micircle.Config
import com.dxp.micircle.domain.router.model.UserModel
import com.dxp.micircle.domain.router.repository.UsersRepository
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class UsersRepositoryImpl @Inject constructor(private val firebaseDatabase: FirebaseDatabase) : UsersRepository {

    override fun getUser(uid: String): Single<UserModel> {

        val subject = SingleSubject.create<UserModel>()
        return subject.doOnSubscribe {

            val userRef = firebaseDatabase.reference.child(Config.FBD_USERS_PATH)
            val userTask = userRef.child(uid).get()

            userTask.addOnCompleteListener { task ->

                if(task.isSuccessful) {
                    subject.onSuccess(task.result.getValue(UserModel::class.java)!!)
                }
                else task.exception?.let { it-> subject.onError(it) } ?: subject.onError(Exception("-1"))
            }
        }
    }
}