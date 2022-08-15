package com.dxp.micircle.data.database.dao

import androidx.room.*
import com.dxp.micircle.data.database.model.FeedEntity

@Dao
interface FeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(feed: FeedEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(feed: List<FeedEntity>)

    @Query("SELECT * FROM feeds WHERE userId = :userId")
    fun getFeeds(userId: String): List<FeedEntity>

    @Query("SELECT * FROM feeds")
    fun getAllFeeds(): List<FeedEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(feed: FeedEntity)

    @Query("DELETE FROM feeds WHERE postId = :id")
    fun delete(id: String)

    @Query("DELETE FROM feeds")
    fun delete()
}