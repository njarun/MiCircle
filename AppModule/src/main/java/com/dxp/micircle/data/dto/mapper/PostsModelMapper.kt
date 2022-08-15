package com.dxp.micircle.data.dto.mapper

import com.dxp.micircle.data.database.model.PostEntity
import com.dxp.micircle.data.dto.mapper.MediaModelMapper.toMediaEntityList
import com.dxp.micircle.domain.router.model.PostModel

object PostsModelMapper {

    fun PostModel.toPostEntity(): PostEntity {

        return PostEntity(
            postId,
            userId,
            timestamp,
            text,
            postPrivacy,
            mediaList?.toMediaEntityList(),
            userName,
            imageUrl
        )
    }
}