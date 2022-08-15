package com.dxp.micircle.data.dto.mapper

import com.dxp.micircle.data.database.model.FeedMediaEntity
import com.dxp.micircle.domain.router.model.FeedMediaModel

object FeedMediaModelMapper {

    fun ArrayList<FeedMediaModel>.toFeedMediaEntityList(): ArrayList<FeedMediaEntity> {

        val modelList = ArrayList<FeedMediaEntity>()

        forEach {

            modelList.add(
                FeedMediaEntity(
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