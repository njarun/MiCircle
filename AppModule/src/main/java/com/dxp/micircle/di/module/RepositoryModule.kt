package com.dxp.micircle.di.module

import com.dxp.micircle.data.database.dao.MediaDao
import com.dxp.micircle.data.database.dao.PostDao
import com.dxp.micircle.data.router.repository.FeedsRepositoryImpl
import com.dxp.micircle.data.router.repository.MiMediaRepositoryImpl
import com.dxp.micircle.data.router.repository.PostsRepositoryImpl
import com.dxp.micircle.data.router.repository.UsersRepositoryImpl
import com.dxp.micircle.domain.router.repository.FeedsRepository
import com.dxp.micircle.domain.router.repository.MiMediaRepository
import com.dxp.micircle.domain.router.repository.PostsRepository
import com.dxp.micircle.domain.router.repository.UsersRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providePostRepository(postDao: PostDao, mediaDao: MediaDao): PostsRepository {
        return PostsRepositoryImpl(postDao, mediaDao)
    }

    @Provides
    fun provideFeedsRepository(FirebaseFirestore: FirebaseFirestore): FeedsRepository {
        return FeedsRepositoryImpl(FirebaseFirestore)
    }

    @Provides
    fun provideUsersRepository(FirebaseFirestore: FirebaseFirestore): UsersRepository {
        return UsersRepositoryImpl(FirebaseFirestore)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class MiMediaRepoModule {
    @Binds
    abstract fun bindMediaEntityData(mediaEntityImpl: MiMediaRepositoryImpl): MiMediaRepository
}