package com.dxp.micircle.data.dto.mapper

import com.dxp.micircle.data.database.model.PostEntity
import com.dxp.micircle.data.dto.mapper.MediaEntityMapper.toMediaModelList
import com.dxp.micircle.domain.router.model.PostModel

object PostsEntityMapper {

    fun List<PostEntity>.toPostModelList(): ArrayList<PostModel> {

        val modelList = ArrayList<PostModel>()

        forEach {

            modelList.add(
                PostModel(
                    it.postId,
                    it.userId,
                    it.timestamp,
                    it.text,
                    it.postPrivacy,
                    it.mediaList?.toMediaModelList()
                )
            )
        }

        return modelList
    }

    fun PostEntity.toPostModel(): PostModel {

        return PostModel(
            postId,
            userId,
            timestamp,
            text,
            postPrivacy,
            mediaList?.toMediaModelList()
        )
    }
}