package com.dxp.micircle.di.module

import android.content.Context
import androidx.room.Room
import com.dxp.micircle.data.database.AppDatabase
import com.dxp.micircle.data.database.dao.MediaDao
import com.dxp.micircle.data.database.dao.PostDao
import com.dxp.micircle.data.router.repository.PostsRepositoryImpl
import com.dxp.micircle.domain.router.repository.PostsRepository
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
    fun providePostRepository(postDao: PostDao, mediaDao: MediaDao): PostsRepository {
        return PostsRepositoryImpl(postDao, mediaDao)
    }
}