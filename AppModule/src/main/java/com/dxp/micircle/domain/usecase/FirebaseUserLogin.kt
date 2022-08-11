package com.dxp.micircle.domain.usecase

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject

class FirebaseUserLogin @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    fun invoke(username: String, password: String) : Single<Boolean> {

        val subject = SingleSubject.create<Boolean>()
        return subject.doOnSubscribe {

            val authCredential = EmailAuthProvider.getCredential(username, password)
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    subject.onSuccess(true)
                }
                else {

                    task.exception?.let { subject.onError(it) } ?: subject.onSuccess(false)
                }
            }
        }
    }
}