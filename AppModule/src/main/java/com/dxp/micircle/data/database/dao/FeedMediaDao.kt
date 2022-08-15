package com.dxp.micircle.data.database.dao

import androidx.room.*
import com.dxp.micircle.data.database.model.FeedMediaEntity

@Dao
interface FeedMediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(medias: ArrayList<FeedMediaEntity>)

    @Query("SELECT * FROM feed_medias WHERE postId = :postId")
    fun getAllMedia(postId: String): List<FeedMediaEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(media: FeedMediaEntity)

    @Query("DELETE FROM feed_medias WHERE postId = :postId")
    fun delete(postId: String)

    @Query("DELETE FROM feed_medias")
    fun delete()
}