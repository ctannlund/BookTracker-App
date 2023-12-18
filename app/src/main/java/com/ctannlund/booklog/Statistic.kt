package com.ctannlund.booklog

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Statistic(
    @PrimaryKey val statName: String,
    var statValue: Int
)
