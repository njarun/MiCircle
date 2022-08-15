package com.dxp.micircle.data.database.model

import androidx.room.Entity

@Entity(tableName = "feed_medias", primaryKeys=["mediaId", "postId"])
data class FeedMediaEntity(
    val mediaId: String,
    val postId: String,
    val type: Int,
    val url: String,
    val size: Long
)