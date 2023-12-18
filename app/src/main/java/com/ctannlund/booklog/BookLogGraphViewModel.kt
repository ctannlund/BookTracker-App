package com.ctannlund.booklog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookLogGraphViewModel : ViewModel() {

    private val bookRepository = BookRepository.get()
    private val _logs: MutableStateFlow<List<Log>> = MutableStateFlow(emptyList())
    val logs: StateFlow<List<Log>>
        get() = _logs.asStateFlow()

    init {
        viewModelScope.launch {
            bookRepository.getLogs().collect {
                _logs.value = it
            }
        }
    }

}