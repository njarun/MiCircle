package com.dxp.micircle.di.module

import com.dxp.micircle.data.router.source.MediaEntityData
import com.dxp.micircle.data.router.source.MediaEntityDataImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class MapperModule {

    @Binds
    abstract fun bindMediaEntityData(mediaEntityImpl: MediaEntityDataImpl): MediaEntityData
}