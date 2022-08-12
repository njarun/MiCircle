package com.dxp.micircle.domain.usecase

import com.dxp.micircle.data.router.CoroutineDispatcherProvider
import com.dxp.micircle.data.router.source.MediaEntityData
import com.dxp.micircle.domain.dto.model.MediaModel
import com.lassi.data.media.MiMedia
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MapMediaToModel @Inject constructor(private val mapperEntityData: MediaEntityData,
                                          private val coroutineDispatcherProvider: CoroutineDispatcherProvider) {

    fun execute(miMediaList: List<MiMedia>) = flow {

        val localNews = mapToMediaModel(miMediaList)

        if (localNews.isNotEmpty()) {

            emit(localNews)
            emit(false)
        }
    }
    .flowOn(coroutineDispatcherProvider.IO())

    private suspend fun mapToMediaModel(miMediaList: List<MiMedia>): ArrayList<MediaModel> {
        return mapperEntityData.getMediaModelList(miMediaList)
    }
}