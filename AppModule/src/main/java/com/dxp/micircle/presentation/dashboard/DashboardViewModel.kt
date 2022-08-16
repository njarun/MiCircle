package com.dxp.micircle.presentation.dashboard

import com.dxp.micircle.data.session.SessionContext
import com.dxp.micircle.domain.helpers.AppSchedulers
import com.dxp.micircle.domain.usecase.FirebaseFetchUser
import com.dxp.micircle.presentation.base.BaseViewModel
import com.dxp.micircle.presentation.base.OnException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth, private val fetchUser: FirebaseFetchUser,
      private val schedulers: AppSchedulers, private val sessionContext: SessionContext) : BaseViewModel() {

    init {

        fetchProfile()
    }

    private fun fetchProfile() {

        firebaseAuth.currentUser?.let {

            subscription {

                fetchUser.execute(it.uid)
                    .subscribeOn(schedulers.ioScheduler)
                    .observeOn(schedulers.uiScheduler)
                    .subscribe({ userModel ->

                        sessionContext.userModel = userModel
                    }, {

                        emitAction(OnException(it))
                    })
            }
        }
    }
}