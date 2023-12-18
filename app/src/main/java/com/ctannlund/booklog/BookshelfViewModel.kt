package com.ctannlund.booklog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookshelfViewModel : ViewModel() {

    private val bookRepository = BookRepository.get()

    private val _books: MutableStateFlow<List<BookshelfBook>> = MutableStateFlow(emptyList())
    val books: StateFlow<List<BookshelfBook>>
        get() = _books.asStateFlow()

    init {
        viewModelScope.launch {
            bookRepository.getBookshelfBooks().collect {
                _books.value = it
            }
        }
    }

}