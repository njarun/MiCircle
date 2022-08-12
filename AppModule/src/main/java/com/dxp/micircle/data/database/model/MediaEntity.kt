package com.dxp.micircle.data.database.model

import androidx.room.Entity

@Entity(tableName = "medias", primaryKeys=["mediaId", "postId"])
data class MediaEntity(
    val mediaId: String,
    val postId: String,
    val type: Int,
    val url: String,
    val size: Long
)