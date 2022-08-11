package com.dxp.micircle.helpers

import com.dxp.micircle.di.annotations.IoScheduler
import com.dxp.micircle.di.annotations.UiScheduler
import io.reactivex.Scheduler
import javax.inject.Inject

data class AppSchedulers @Inject constructor(@IoScheduler val ioScheduler: Scheduler, @UiScheduler val uiScheduler: Scheduler)