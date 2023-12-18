package com.ctannlund.booklog

import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private val bookRepository = BookRepository.get()

    private suspend fun getGoal(goalLength: String): ReadingGoal {
        return bookRepository.getReadingGoal(goalLength)
    }

    suspend fun resetGoals() {
        bookRepository.deleteReadingGoal(getGoal("yearly"))
        bookRepository.deleteReadingGoal(getGoal("monthly"))
        bookRepository.deleteReadingGoal(getGoal("weekly"))
        bookRepository.deleteReadingGoal(getGoal("daily"))
    }

}