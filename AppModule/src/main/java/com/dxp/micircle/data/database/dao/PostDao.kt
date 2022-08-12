package com.dxp.micircle.data.database.dao

import androidx.room.*
import com.dxp.micircle.data.database.model.PostEntity

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: PostEntity)

    @Query("SELECT * FROM posts WHERE postId = :postId")
    fun getPost(postId: String): PostEntity?

    @Query("SELECT * FROM posts")
    fun getAllPosts(): List<PostEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(post: PostEntity)

    @Query("DELETE FROM posts WHERE postId = :id")
    fun delete(id: String)

    @Query("DELETE FROM posts")
    fun delete()
}