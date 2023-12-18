package com.ctannlund.booklog.database

import androidx.room.*
import com.ctannlund.booklog.BookshelfBook
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface BookshelfDao {

    @Query("SELECT * FROM bookshelfbook")
    fun getBooks(): Flow<List<BookshelfBook>>

    @Query("SELECT * FROM bookshelfbook WHERE id=(:id)")
    suspend fun getBookshelfBook(id: UUID): BookshelfBook

    @Update
    suspend fun updateBookshelfBook(bookshelfBook: BookshelfBook)

    @Insert
    suspend fun addBookshelfBook(bookshelfBook: BookshelfBook)

    @Delete
    suspend fun deleteBookshelfBook(bookshelfBook: BookshelfBook)


}