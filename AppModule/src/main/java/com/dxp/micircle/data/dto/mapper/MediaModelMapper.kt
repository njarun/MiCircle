package com.dxp.micircle.data.dto.mapper

import com.dxp.micircle.data.database.model.MediaEntity
import com.dxp.micircle.domain.router.model.MediaModel

object MediaModelMapper {

    fun ArrayList<MediaModel>.toMediaEntityList(): ArrayList<MediaEntity> {

        val modelList = ArrayList<MediaEntity>()

        forEach {

            modelList.add(
                MediaEntity(
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

    fun MediaModel.toMediaEntity(): MediaEntity {

        return MediaEntity(
            mediaId,
            postId,
            type,
            url,
            size,
        )
    }
}