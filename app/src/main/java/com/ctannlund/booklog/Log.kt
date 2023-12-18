package com.ctannlund.booklog

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Log(
    @PrimaryKey val id:UUID,
    val title:String, val author:String,
    val pages: Int,
    val date: Date) {
}
