package com.dxp.micircle.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject

class FirebaseValidateUser @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    operator fun invoke() = firebaseAuth.currentUser?.let { currentUser ->

        val subject = SingleSubject.create<Boolean>()
        subject.doOnSubscribe {

            currentUser.getIdToken(false).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    subject.onSuccess(true)
                }
                else {
                    task.exception?.let { subject.onError(it) }
                }
            }
        }
    } ?: Single.just(false)
}