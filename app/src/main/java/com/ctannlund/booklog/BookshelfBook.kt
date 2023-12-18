package com.ctannlund.booklog

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class BookshelfBook(

    @PrimaryKey val id:UUID,
    val title:String, val author:String,
    val currentPages: Int, val totalPages: Int,
    val startDate: Date, val endDate: Date,
    val bookComplete: Boolean) {

}


