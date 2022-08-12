package com.dxp.micircle.data.router.repository

import com.dxp.micircle.data.dto.mapper.MiMediaMapper.toMediaModelList
import com.dxp.micircle.domain.router.model.MediaModel
import com.dxp.micircle.domain.router.repository.MiMediaRepository
import com.lassi.data.media.MiMedia
import javax.inject.Inject

class MiMediaRepositoryImpl @Inject constructor() : MiMediaRepository {

    override suspend fun getMediaModelList(miMediaList: List<MiMedia>): ArrayList<MediaModel> {
        return miMediaList.toMediaModelList()
    }
}