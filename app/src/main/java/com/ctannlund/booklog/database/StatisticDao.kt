package com.ctannlund.booklog.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ctannlund.booklog.Statistic
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticDao {

    @Query("SELECT * FROM statistic")
    fun getStats(): Flow<List<Statistic>>

    @Query("SELECT * FROM statistic WHERE statName=(:statName)")
    suspend fun getStat(statName: String): Statistic

    @Insert
    suspend fun addStat(statistic: Statistic)

    @Update
    suspend fun updateStat(statistic: Statistic)

}