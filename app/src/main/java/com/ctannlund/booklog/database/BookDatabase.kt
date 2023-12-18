package com.ctannlund.booklog.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ctannlund.booklog.*

@Database(entities = [Book::class, ReadingGoal::class, Statistic::class, Log::class,
                     BookshelfBook::class], version=1)
@TypeConverters(BookTypeConverters::class)
abstract class BookDatabase:RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun readingGoalDao(): ReadingGoalDao
    abstract fun statisticDao(): StatisticDao
    abstract fun logDao(): LogDao
    abstract fun bookshelfDao(): BookshelfDao

}