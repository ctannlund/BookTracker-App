package com.ctannlund.booklog.database

import androidx.room.*
import com.ctannlund.booklog.Book
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface BookDao {

    @Query("SELECT * FROM book")
    fun getBooks(): Flow<List<Book>>

    @Query("SELECT * FROM book WHERE id=(:id)")
    suspend fun getBook(id: UUID): Book

    @Update
    suspend fun updateBook(book: Book)

    @Insert
    suspend fun addBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)


}