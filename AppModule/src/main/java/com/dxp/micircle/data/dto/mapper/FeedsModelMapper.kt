package com.dxp.micircle.data.dto.mapper

import com.dxp.micircle.data.database.model.FeedEntity
import com.dxp.micircle.data.dto.mapper.FeedMediaModelMapper.toFeedMediaEntityList
import com.dxp.micircle.domain.router.model.FeedModel

object FeedsModelMapper {

    fun List<FeedModel>.toFeedEntityList(): ArrayList<FeedEntity> {

        val modelList = ArrayList<FeedEntity>()

        forEach {

            modelList.add(
                FeedEntity(
                    it.postId,
                    it.userId,
                    it.timestamp,
                    it.text,
                    it.postPrivacy,
                    it.mediaList?.toFeedMediaEntityList(),
                    it.userName,
                    it.imageUrl
                )
            )
        }

        return modelList
    }

    fun FeedModel.toFeedEntity(): FeedEntity {

        return FeedEntity(
            postId,
            userId,
            timestamp,
            text,
            postPrivacy,
            mediaList?.toFeedMediaEntityList(),
            userName,
            imageUrl
        )
    }
}