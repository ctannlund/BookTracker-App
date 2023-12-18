package com.ctannlund.booklog

import androidx.lifecycle.ViewModel
import org.joda.time.DateTime
import java.util.*

class GoalSetterViewModel : ViewModel() {

    private val bookRepository = BookRepository.get()

    suspend fun addGoal(readingGoal: ReadingGoal) {
        bookRepository.addReadingGoal(readingGoal)
    }

    fun calculateDeadline(goalLength: String): Date {

        val currentMidnight = DateTime().withTimeAtStartOfDay()
        val nextMidnight = currentMidnight.plusDays(1).withTimeAtStartOfDay()

        val daysToNextMon = 8 - (DateTime().withTimeAtStartOfDay().dayOfWeek)
        val nextMonday = currentMidnight.plusDays(daysToNextMon).withTimeAtStartOfDay()

        val daysToEndMonth = (DateTime().dayOfMonth().maximumValue + 1) - (DateTime().withTimeAtStartOfDay().dayOfMonth)
        val nextMonth = currentMidnight.plusDays(daysToEndMonth).withTimeAtStartOfDay()

        val daysToEndYear = 1 + (DateTime().dayOfYear().maximumValue) - (DateTime().withTimeAtStartOfDay().dayOfYear)
        val nextYear = currentMidnight.plusDays(daysToEndYear).withTimeAtStartOfDay()

        return when (goalLength) {
            "daily" -> nextMidnight.toDate()
            "weekly" -> nextMonday.toDate()
            "monthly" -> nextMonth.toDate()
            "yearly" -> nextYear.toDate()
            else -> nextMidnight.toDate()
        }
    }

}