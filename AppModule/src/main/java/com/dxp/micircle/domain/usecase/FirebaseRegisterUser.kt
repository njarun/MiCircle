package com.dxp.micircle.domain.usecase

import com.dxp.micircle.Config
import com.dxp.micircle.data.dto.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject


class FirebaseRegisterUser @Inject constructor(private val firebaseAuth: FirebaseAuth,
                                        private var FirebaseFirestore: FirebaseFirestore) {

    fun invoke(fName: String, lName: String, username: String, password: String) : Single<Boolean> {

        val subject = SingleSubject.create<Boolean>()
        return subject.doOnSubscribe {

            firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener{ task ->

                    if (task.isSuccessful) {

                        firebaseAuth.currentUser?.let {

                            val postRef = FirebaseFirestore.collection(Config.FBD_USERS_PATH)
                                .document(it.uid)

                            val userModel = UserModel(it.uid,
                                System.currentTimeMillis(),
                                fName,
                                lName,
                                null)

                            postRef.set(userModel).addOnCompleteListener { updateResponse ->

                                if (updateResponse.isSuccessful) {

                                    subject.onSuccess(true)
                                }
                                else {

                                    task.exception?.let { it-> subject.onError(it) } ?: subject.onSuccess(false)
                                    it.delete()
                                }
                            }
                        } ?: subject.onSuccess(false)
                    }
                    else {

                        task.exception?.let { it -> subject.onError(it) } ?: subject.onSuccess(false)
                    }
                }
        }
    }
}