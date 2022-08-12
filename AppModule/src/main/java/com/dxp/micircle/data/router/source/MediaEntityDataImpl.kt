package com.dxp.micircle.data.router.source

import com.dxp.micircle.data.dto.mapper.NewsEntityMapper.toMediaModelList
import com.dxp.micircle.domain.dto.model.MediaModel
import com.lassi.data.media.MiMedia
import javax.inject.Inject

class MediaEntityDataImpl @Inject constructor() : MediaEntityData {

    override suspend fun getMediaModelList(miMediaList: List<MiMedia>): ArrayList<MediaModel> {
        return miMediaList.toMediaModelList()
    }
}