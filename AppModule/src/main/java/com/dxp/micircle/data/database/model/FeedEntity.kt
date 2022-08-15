package com.dxp.micircle.data.database.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "feeds")
data class FeedEntity(
    @PrimaryKey
    val postId: String,
    val userId: String,
    val timestamp: Long,
    val text: String?,
    val postPrivacy: Int,
    val userName:String?,
    val imageUrl: String?) {

    @Ignore
    var mediaList: ArrayList<FeedMediaEntity>? = null

    constructor(pId: String, uId: String, ts: Long, txt: String?, pp: Int, mL: ArrayList<FeedMediaEntity>?, uN: String?, profUrl: String?) : this(pId, uId, ts, txt, pp, uN, profUrl) {
        this.mediaList = mL
    }
}