package com.ctannlund.booklog

import android.content.Context
import androidx.room.Room
import com.ctannlund.booklog.database.BookDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

private const val DATABASE_NAME = "book-database"
class BookRepository private constructor(context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope) {

    private val database: BookDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            BookDatabase::class.java,
            DATABASE_NAME
        )
        .build()

    // ----------------------------

    // books
    fun getBooks(): Flow<List<Book>> = database.bookDao().getBooks()

    suspend fun getBook(id: UUID): Book = database.bookDao().getBook(id)

    fun updateBook(book: Book) {
        coroutineScope.launch {
            database.bookDao().updateBook(book)
        }
    }

    suspend fun addBook(book: Book) {
        database.bookDao().addBook(book)
    }

    suspend fun deleteBook(book: Book) {
        database.bookDao().deleteBook(book)
    }

    suspend fun getReadingGoal(goalLength: String): ReadingGoal =
        database.readingGoalDao().getReadingGoal(goalLength)

    fun updateReadingGoal(readingGoal: ReadingGoal) {
        coroutineScope.launch {
            database.readingGoalDao().updateReadingGoal(readingGoal)
        }
    }

    suspend fun addReadingGoal(readingGoal: ReadingGoal) {
        database.readingGoalDao().addReadingGoal(readingGoal)
    }

    suspend fun deleteReadingGoal(readingGoal: ReadingGoal) {
        database.readingGoalDao().deleteReadingGoal(readingGoal)
    }

    suspend fun getStat(statName: String): Statistic = database.statisticDao().getStat(statName)

    suspend fun addStat(stat: Statistic) {
        database.statisticDao().addStat(stat)
    }

    fun updateStat(stat: Statistic) {
        coroutineScope.launch {
            database.statisticDao().updateStat(stat)
        }
    }

    // logs
    fun getLogs(): Flow<List<Log>> = database.logDao().getLogs()

    suspend fun addLog(log: Log) {
        database.logDao().addLog(log)
    }

    // shelf
    fun getBookshelfBooks(): Flow<List<BookshelfBook>> = database.bookshelfDao().getBooks()

    suspend fun getBookshelfBook(id: UUID): BookshelfBook = database.bookshelfDao().getBookshelfBook(id)

    fun updateBookshelfBook(bookshelfBook: BookshelfBook) {
        coroutineScope.launch {
            database.bookshelfDao().updateBookshelfBook(bookshelfBook)
        }
    }

    suspend fun addBookshelfBook(bookshelfBook: BookshelfBook) {
        database.bookshelfDao().addBookshelfBook(bookshelfBook)
    }

    suspend fun deleteBookshelfBook(bookshelfBook: BookshelfBook) {
        database.bookshelfDao().deleteBookshelfBook(bookshelfBook)
    }


    // ----------------------------

    companion object {
        private var INSTANCE: BookRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = BookRepository(context)
            }
        }

        fun get(): BookRepository {
            return INSTANCE ?:
            throw java.lang.IllegalStateException("BookRepo must be initialized!")
        }

    }
}