package com.ctannlund.booklog.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ctannlund.booklog.Log
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface LogDao {

    @Query("SELECT * FROM log")
    fun getLogs(): Flow<List<Log>>

    @Query("SELECT * FROM log WHERE id=(:id)")
    suspend fun getLog(id: UUID): Log

    @Update
    suspend fun updateLog(log: Log)

    @Insert
    suspend fun addLog(log: Log)


}