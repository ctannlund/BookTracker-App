package com.ctannlund.booklog

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class ReadingGoal(

    @PrimaryKey val goalLength: String, // daily, weekly, monthly, yearly
    val goalType: String, // pages or books
    val goalNumber: Int,
    val goalDeadline: Date, // time to countdown from
    val goalProgress: Int,
    val goalSuccessCount: Int, // use for stats page to track # of times a goal has been done
    val goalCompleted: Boolean

) {

}