package com.ctannlund.booklog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class PageAdderViewModel(bookId: UUID) : ViewModel() {

    private val bookRepository = BookRepository.get()

    private val _book:MutableStateFlow<Book?> = MutableStateFlow(null)
    val book:StateFlow<Book?> = _book.asStateFlow()

    private val _bookshelfBook:MutableStateFlow<BookshelfBook?> = MutableStateFlow(null)
    val bookshelfBook:StateFlow<BookshelfBook?> = _bookshelfBook.asStateFlow()

    val _goalDaily:MutableStateFlow<ReadingGoal?> = MutableStateFlow(null)
    private val goalDaily:StateFlow<ReadingGoal?> = _goalDaily.asStateFlow()
    val _goalWeekly:MutableStateFlow<ReadingGoal?> = MutableStateFlow(null)
    private val goalWeekly:StateFlow<ReadingGoal?> = _goalWeekly.asStateFlow()
    val _goalMonthly:MutableStateFlow<ReadingGoal?> = MutableStateFlow(null)
    private val goalMonthly:StateFlow<ReadingGoal?> = _goalMonthly.asStateFlow()
    val _goalYearly:MutableStateFlow<ReadingGoal?> = MutableStateFlow(null)
    private val goalYearly:StateFlow<ReadingGoal?> = _goalYearly.asStateFlow()

    init {
        viewModelScope.launch {
            _book.value = bookRepository.getBook(bookId)
            _bookshelfBook.value = bookRepository.getBookshelfBook(bookId)
            _goalDaily.value = bookRepository.getReadingGoal("daily")
            _goalWeekly.value = bookRepository.getReadingGoal("weekly")
            _goalMonthly.value = bookRepository.getReadingGoal("monthly")
            _goalYearly.value = bookRepository.getReadingGoal("yearly")
        }
    }

    suspend fun addStat(stat: Statistic) {
        bookRepository.addStat(stat)
    }

    suspend fun addLog(log: Log) {
        bookRepository.addLog(log)
    }

    suspend fun getStat(statName: String): Statistic {
        return bookRepository.getStat(statName)
    }

    suspend fun getBook(bookId: UUID): Book {
        return bookRepository.getBook(bookId)
    }

    suspend fun getBookshelfBook(bookshelfId: UUID): BookshelfBook {
        return bookRepository.getBookshelfBook(bookshelfId)
    }

    suspend fun deleteBook(book: Book) {
        bookRepository.deleteBook(book)
    }

    suspend fun deleteBookshelfBook(bookshelfBook: BookshelfBook) {
        bookRepository.deleteBookshelfBook(bookshelfBook)
    }

    fun updateBook(onUpdate: (Book) -> Book) {
        _book.update { prevBook ->
            prevBook?.let { onUpdate(it) }
        }
    }

    fun updateReadingGoal(goalNum: MutableStateFlow<ReadingGoal?>, onUpdate: (ReadingGoal) -> ReadingGoal) {
        goalNum.update { prevGoal ->
            prevGoal?.let { onUpdate(it) }
        }
    }

    fun updateBookshelfBook(onUpdate: (BookshelfBook) -> BookshelfBook) {
        _bookshelfBook.update { prevBook ->
            prevBook?.let { onUpdate(it) }
        }
    }

    fun checkBookComplete(book: Book, wheelNum: Int): Boolean {
        return wheelNum >= book.totalPages
    }

    suspend fun getPageMaxCount(bookId: UUID): Int {
        val bookToCount = getBook(bookId)
        return bookToCount.totalPages
    }

    suspend fun getPageMinCount(bookId: UUID): Int {
        val bookToCount = getBook(bookId)
        return bookToCount.currentPages
    }

    fun updateStat(statNum: MutableStateFlow<Statistic?>, onUpdate: (Statistic) -> Statistic) {
        statNum.update { prevStat ->
            prevStat?.let { onUpdate(it) }
        }
        val statUpdate:StateFlow<Statistic?> = statNum.asStateFlow()
        statUpdate.value?.let { bookRepository.updateStat(it) }
    }

    override fun onCleared() {
        super.onCleared()
        book.value?.let { bookRepository.updateBook(it) }
        bookshelfBook.value?.let { bookRepository.updateBookshelfBook(it) }
        goalDaily.value?.let { bookRepository.updateReadingGoal(it) }
        goalWeekly.value?.let { bookRepository.updateReadingGoal(it) }
        goalMonthly.value?.let { bookRepository.updateReadingGoal(it) }
        goalYearly.value?.let { bookRepository.updateReadingGoal(it) }
    }

}

class PageAdderViewModelFactory(private val bookId: UUID) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PageAdderViewModel(bookId) as T
    }

}