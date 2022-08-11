package com.dxp.micircle.di.module

import com.dxp.micircle.di.annotations.IoScheduler
import com.dxp.micircle.di.annotations.UiScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {

    @IoScheduler
    @Singleton
    @Provides()
    fun provideIoScheduler(): Scheduler = Schedulers.io()

    @UiScheduler
    @Singleton
    @Provides
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()
}