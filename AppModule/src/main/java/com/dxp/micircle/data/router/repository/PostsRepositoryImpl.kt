package com.dxp.micircle.data.router.repository

import com.dxp.micircle.data.database.dao.MediaDao
import com.dxp.micircle.data.database.dao.PostDao
import com.dxp.micircle.data.database.model.MediaEntity
import com.dxp.micircle.data.dto.mapper.MediaModelMapper.toMediaEntity
import com.dxp.micircle.data.dto.mapper.PostsEntityMapper.toPostModel
import com.dxp.micircle.data.dto.mapper.PostsEntityMapper.toPostModelList
import com.dxp.micircle.data.dto.mapper.PostsModelMapper.toPostEntity
import com.dxp.micircle.domain.router.model.MediaModel
import com.dxp.micircle.domain.router.model.PostModel
import com.dxp.micircle.domain.router.repository.PostsRepository
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(private val postDao: PostDao, private val mediaDao: MediaDao) : PostsRepository {

    override suspend fun getPost(postId: String): PostModel? {

        val post = postDao.getPost(postId)

        post?.let {
            post.mediaList = mediaDao.getAllMedia(postId) as ArrayList<MediaEntity>
        }

        return post?.toPostModel()
    }

    override suspend fun getPostsList(): ArrayList<PostModel> {

        val posts = postDao.getAllPosts()
        posts.forEach {
            it.mediaList = mediaDao.getAllMedia(it.postId) as ArrayList<MediaEntity>
        }

        return posts.toPostModelList()
    }

    override suspend fun savePost(postModel: PostModel) {

        val postEntity = postModel.toPostEntity()
        postDao.insert(postEntity)
        postEntity.mediaList?.let { mediaDao.insert(it) }
    }

    override suspend fun updateMedia(mediaModel: MediaModel) {

        val mediaEntity = mediaModel.toMediaEntity()
        mediaDao.update(mediaEntity)
    }

    override suspend fun deletePost(postId: String) {

        postDao.delete(postId)
        mediaDao.delete(postId)
    }

    override suspend fun deleteAllPosts() {

        postDao.delete()
        mediaDao.delete()
    }
}