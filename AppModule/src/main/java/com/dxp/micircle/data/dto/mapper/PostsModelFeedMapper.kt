package com.dxp.micircle.data.dto.mapper

import com.dxp.micircle.data.dto.mapper.MediaModelFeedMediaMapper.toFeedMediaModelList
import com.dxp.micircle.domain.router.model.FeedModel
import com.dxp.micircle.domain.router.model.PostModel

object PostsModelFeedMapper {

    fun PostModel.toFeedModel(): FeedModel {

        return FeedModel(
            postId,
            userId,
            timestamp,
            text,
            postPrivacy,
            mediaList?.toFeedMediaModelList(),
            null,
            null)
    }
}