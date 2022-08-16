package com.dxp.micircle.data.dto.mapper

import com.dxp.micircle.data.dto.model.FeedMediaModel
import com.dxp.micircle.domain.router.model.MediaModel

object MediaModelFeedMediaMapper {

    fun ArrayList<MediaModel>.toFeedMediaModelList(): ArrayList<FeedMediaModel> {

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