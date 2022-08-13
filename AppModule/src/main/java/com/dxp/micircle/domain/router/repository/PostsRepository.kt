package com.dxp.micircle.domain.router.repository

import com.dxp.micircle.domain.router.model.MediaModel
import com.dxp.micircle.domain.router.model.PostModel

interface PostsRepository {

    suspend fun getPost(postId: String): PostModel?

    suspend fun getPostsList(): ArrayList<PostModel>

    suspend fun savePost(postModel: PostModel)

    suspend fun updateMedia(mediaModel: MediaModel)

    suspend fun deletePost(postId: String)

    suspend fun deleteAllPosts()
}