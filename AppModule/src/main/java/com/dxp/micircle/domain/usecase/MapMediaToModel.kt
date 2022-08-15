package com.dxp.micircle.domain.usecase

import com.dxp.micircle.data.router.CoroutineDispatcherProvider
import com.dxp.micircle.domain.router.model.MediaModel
import com.dxp.micircle.domain.router.repository.MiMediaRepository
import com.lassi.data.media.MiMedia
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MapMediaToModel @Inject constructor(private val mapperEntityData: MiMediaRepository,
                                          private val coroutineDispatcherProvider: CoroutineDispatcherProvider) {

    fun execute(miMediaList: List<MiMedia>) = flow {

        val localMedia = mapToMediaModel(miMediaList)

        if (localMedia.isNotEmpty()) {

            emit(localMedia)
            emit(false)
        }
    }
    .flowOn(coroutineDispatcherProvider.IO())

    private suspend fun mapToMediaModel(miMediaList: List<MiMedia>): ArrayList<MediaModel> {
        return mapperEntityData.getMediaModelList(miMediaList)
    }
}