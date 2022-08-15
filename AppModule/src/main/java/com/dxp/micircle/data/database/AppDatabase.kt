package com.dxp.micircle.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dxp.micircle.data.database.dao.FeedDao
import com.dxp.micircle.data.database.dao.FeedMediaDao
import com.dxp.micircle.data.database.dao.MediaDao
import com.dxp.micircle.data.database.dao.PostDao
import com.dxp.micircle.data.database.model.FeedEntity
import com.dxp.micircle.data.database.model.FeedMediaEntity
import com.dxp.micircle.data.database.model.MediaEntity
import com.dxp.micircle.data.database.model.PostEntity

@Database(
    version = 1,
    entities = [PostEntity::class, MediaEntity::class, FeedEntity::class, FeedMediaEntity::class],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    abstract fun mediaDao(): MediaDao

    abstract fun feedDao(): FeedDao

    abstract fun feedMediaDao(): FeedMediaDao
}