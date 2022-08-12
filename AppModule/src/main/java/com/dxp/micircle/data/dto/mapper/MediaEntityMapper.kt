package com.dxp.micircle.data.dto.mapper

import com.dxp.micircle.data.database.model.MediaEntity
import com.dxp.micircle.domain.router.model.MediaModel

object MediaEntityMapper {

    fun List<MediaEntity>.toMediaModelList(): ArrayList<MediaModel> {

        val modelList = ArrayList<MediaModel>()

        forEach {

            modelList.add(
                MediaModel(
                    it.mediaId,
                    it.postId,
                    it.type,
                    it.url,
                    it.size,
                )
            )
        }

        return modelList
    }
}