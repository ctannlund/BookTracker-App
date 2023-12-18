package com.ctannlund.booklog.database

import androidx.room.*
import com.ctannlund.booklog.ReadingGoal
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ReadingGoalDao {

    @Query("SELECT * FROM readinggoal")
    fun getReadingGoals(): Flow<List<ReadingGoal>>

    @Query("SELECT * FROM readinggoal WHERE goalLength=(:goalLength)")
    suspend fun getReadingGoal(goalLength: String): ReadingGoal

    @Update
    suspend fun updateReadingGoal(readingGoal: ReadingGoal)

    @Insert
    suspend fun addReadingGoal(readingGoal: ReadingGoal)

    @Delete
    suspend fun deleteReadingGoal(readingGoal: ReadingGoal)

}