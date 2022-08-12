package com.dxp.micircle.data.database.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
    val postId: String,
    val userId: String,
    val timestamp: Long,
    val text: String?,
    val postPrivacy: Int) {

    @Ignore
    var mediaList: ArrayList<MediaEntity>? = null

    constructor(pId: String, uId: String, ts: Long, txt: String?, pp: Int, mL: ArrayList<MediaEntity>?) : this(pId, uId, ts, txt, pp) {
        this.mediaList = mL
    }
}