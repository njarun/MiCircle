package com.dxp.micircle.domain.router.repository

import com.dxp.micircle.domain.router.model.MediaModel
import com.lassi.data.media.MiMedia

interface MiMediaRepository {

    suspend fun getMediaModelList(miMediaList: List<MiMedia>): ArrayList<MediaModel>
}