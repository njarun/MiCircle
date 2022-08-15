package com.dxp.micircle.di.module

import android.content.Context
import androidx.room.Room
import com.dxp.micircle.data.database.AppDatabase
import com.dxp.micircle.data.database.dao.FeedDao
import com.dxp.micircle.data.database.dao.FeedMediaDao
import com.dxp.micircle.data.database.dao.MediaDao
import com.dxp.micircle.data.database.dao.PostDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "app_storage_bucket"

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    fun providePostDao(appDatabase: AppDatabase): PostDao {
        return appDatabase.postDao()
    }

    @Provides
    fun provideMediaDao(appDatabase: AppDatabase): MediaDao {
        return appDatabase.mediaDao()
    }

    @Provides
    fun provideFeedsDao(appDatabase: AppDatabase): FeedDao {
        return appDatabase.feedDao()
    }

    @Provides
    fun provideFeedMediaDao(appDatabase: AppDatabase): FeedMediaDao {
        return appDatabase.feedMediaDao()
    }
}