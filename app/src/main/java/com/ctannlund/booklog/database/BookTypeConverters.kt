package com.ctannlund.booklog.database

import androidx.room.TypeConverter
import java.util.*

class BookTypeConverters {

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }

}