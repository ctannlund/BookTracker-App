package com.ctannlund.booklog

import androidx.lifecycle.ViewModel

class BookAddCustomViewModel : ViewModel() {

    private val bookRepository = BookRepository.get()

    suspend fun addBook(book: Book) {
        bookRepository.addBook(book)
    }

    suspend fun addBookshelfBook(bookshelfBook: BookshelfBook) {
        bookRepository.addBookshelfBook(bookshelfBook)
    }

}