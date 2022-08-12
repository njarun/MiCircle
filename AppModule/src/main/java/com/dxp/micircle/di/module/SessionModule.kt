package com.dxp.micircle.di.module

import android.content.Context
import androidx.work.WorkManager
import com.dxp.micircle.data.router.CoroutineDispatcherProvider
import com.dxp.micircle.di.annotations.IoScheduler
import com.dxp.micircle.di.annotations.UiScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {

    @Provides
    fun provideCoroutineDispatcher() = CoroutineDispatcherProvider()

    @IoScheduler
    @Singleton
    @Provides()
    fun provideIoScheduler(): Scheduler = Schedulers.io()

    @UiScheduler
    @Singleton
    @Provides
    fun provideUiScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Singleton
    @Provides
    fun provideWorkerManager(@ApplicationContext context: Context): WorkManager = WorkManager.getInstance(context)
}