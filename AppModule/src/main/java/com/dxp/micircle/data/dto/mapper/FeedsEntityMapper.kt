package com.dxp.micircle.data.dto.mapper

import com.dxp.micircle.data.database.model.FeedEntity
import com.dxp.micircle.data.dto.mapper.FeedMediaEntityMapper.toFeedMediaModelList
import com.dxp.micircle.domain.router.model.FeedModel

object FeedsEntityMapper {

    fun List<FeedEntity>.toFeedModelList(): ArrayList<FeedModel> {

        val modelList = ArrayList<FeedModel>()

        forEach {

            modelList.add(
                FeedModel(
                    it.postId,
                    it.userId,
                    it.timestamp,
                    it.text,
                    it.postPrivacy,
                    it.mediaList?.toFeedMediaModelList(),
                    it.userName,
                    it.imageUrl
                )
            )
        }

        return modelList
    }
}