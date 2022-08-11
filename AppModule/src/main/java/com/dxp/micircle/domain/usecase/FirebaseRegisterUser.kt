package com.dxp.micircle.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject


class FirebaseRegisterUser @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun invoke(fName: String, lName: String, username: String, password: String) : Single<Boolean> {

        val subject = SingleSubject.create<Boolean>()
        return subject.doOnSubscribe {

            firebaseAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener{ task ->

                    if (task.isSuccessful) {

                        val profUpdateReq = UserProfileChangeRequest.Builder().setDisplayName("$fName $lName").build()
                        firebaseAuth.currentUser?.let {

                            it.updateProfile(profUpdateReq)
                                .addOnCompleteListener { updateResponse ->

                                    if (updateResponse.isSuccessful) {

                                        subject.onSuccess(true)
                                    }
                                    else {

                                        task.exception?.let { it-> subject.onError(it) } ?: subject.onSuccess(false)
                                        it.delete()
                                    }
                                }
                        }
                    }
                    else {

                        task.exception?.let { it -> subject.onError(it) } ?: subject.onSuccess(false)
                    }
                }
        }
    }
}