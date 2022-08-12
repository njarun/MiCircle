package com.dxp.micircle.data.database.dao

import androidx.room.*
import com.dxp.micircle.data.database.model.MediaEntity

@Dao
interface MediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(medias: List<MediaEntity>)

    @Query("SELECT * FROM medias WHERE postId = :postId")
    fun getAllMedia(postId: String): List<MediaEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(media: MediaEntity)

    @Query("DELETE FROM medias WHERE postId = :postId")
    fun delete(postId: String)

    @Query("DELETE FROM medias")
    fun delete()
}