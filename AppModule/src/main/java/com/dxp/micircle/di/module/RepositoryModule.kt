package com.dxp.micircle.di.module

import com.dxp.micircle.data.router.repository.MiMediaRepositoryImpl
import com.dxp.micircle.domain.router.repository.MiMediaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMediaEntityData(mediaEntityImpl: MiMediaRepositoryImpl): MiMediaRepository

    /*@Binds
    abstract fun bindPostsData(postsRepositoryImpl: PostsRepositoryImpl): PostsRepository*/
}