package com.dxp.micircle.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import javax.inject.Inject

class FirebaseUserLogout @Inject constructor(private val firebaseAuth: FirebaseAuth) {

    operator fun invoke() : Single<Boolean> {

        val subject = SingleSubject.create<Boolean>()
        return subject.doOnSubscribe {

            try {

                firebaseAuth.addAuthStateListener {
                    it.currentUser?.let { subject.onSuccess(false) } ?: subject.onSuccess(true)
                }

                firebaseAuth.signOut()
            }
            catch (e: Exception) {

                subject.onError(e)
            }
        }
    }
}