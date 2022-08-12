package com.dxp.micircle.data.dto.mapper

import com.dxp.micircle.domain.dto.model.MediaModel
import com.lassi.data.media.MiMedia

object NewsEntityMapper {

    fun List<MiMedia>.toMediaModelList(): ArrayList<MediaModel> {

        val newsList = ArrayList<MediaModel>()

        forEach {
            newsList.add(
                MediaModel(
                    it.id,
                    it.name,
                    it.path,
                    it.duration,
                    it.thumb,
                    it.fileSize,
                    it.doesUri
                )
            )
        }

        return newsList
    }
}