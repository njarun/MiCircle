package com.dxp.micircle.data.dto.mapper

import com.dxp.micircle.data.database.model.FeedMediaEntity
import com.dxp.micircle.data.dto.model.FeedMediaModel

object FeedMediaEntityMapper {

    fun List<FeedMediaEntity>.toFeedMediaModelList(): ArrayList<FeedMediaModel> {

        val modelList = ArrayList<FeedMediaModel>()

        forEach {

            modelList.add(
                FeedMediaModel(
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