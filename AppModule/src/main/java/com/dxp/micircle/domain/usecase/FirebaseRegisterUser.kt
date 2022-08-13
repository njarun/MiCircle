package com.dxp.micircle.domain.usecase

import com.dxp.micircle.Config
import com.dxp.micircle.data.router.CoroutineDispatcherProvider
import com.dxp.micircle.domain.router.model.UserModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


@Suppress("BlockingMethodInNonBlockingContext")
class FirebaseRegisterUser @Inject constructor(private val firebaseAuth: FirebaseAuth,
                                               private val coroutineDispatcherProvider: CoroutineDispatcherProvider) {

    @Inject
    lateinit var firebaseDatabase: FirebaseDatabase

    fun execute(fName: String, lName: String, username: String, password: String) = flow {

        try {

            val task = firebaseAuth.createUserWithEmailAndPassword(username, password)
            Tasks.await(task)

            if (task.isSuccessful) {

                firebaseAuth.currentUser?.let {

                    val postRef = firebaseDatabase.reference.child(Config.FBD_USERS_PATH)
                        .child(it.uid)

                    val userModel = UserModel(it.uid,
                        System.currentTimeMillis(),
                        fName,
                        lName,
                        null)

                    val postTask = postRef.setValue(userModel)
                    Tasks.await(postTask)

                    if (postTask.isSuccessful) {

                        emit(false)
                    }
                    else {

                        it.delete()
                        throw postTask.exception ?: Exception("-1")
                    }
                } ?: throw Exception("-1")
            }
            else {

                throw task.exception ?: Exception("-1")
            }
        }
        catch (e: Exception) {

            throw e
        }
    }
    .flowOn(coroutineDispatcherProvider.IO())
}