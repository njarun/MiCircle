package com.dxp.micircle.data.router.source

import com.dxp.micircle.domain.dto.model.MediaModel
import com.lassi.data.media.MiMedia

interface MediaEntityData {

    suspend fun getMediaModelList(miMediaList: List<MiMedia>): ArrayList<MediaModel>
}