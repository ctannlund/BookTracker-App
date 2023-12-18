package com.ctannlund.booklog

import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.math.roundToInt

class BookGoalStatsViewModel : ViewModel() {

    private val bookRepository = BookRepository.get()

    private val headerList = listOf(
        GoalHeader("Daily Goal", "daily"),
        GoalHeader("Weekly Goal", "weekly"),
        GoalHeader("Monthly Goal", "monthly"),
        GoalHeader("Yearly Goal", "yearly")
    )

    private var currentIndex = 0

    val currentHeaderLength: String
        get() = headerList[currentIndex].goalLength

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % headerList.size
    }

    fun moveToPrev() {
        currentIndex = if (currentIndex > 0) {
            (currentIndex - 1) % headerList.size
        } else {
            headerList.size - 1
        }
    }

    suspend fun getGoal(goalLength: String): ReadingGoal {
        return bookRepository.getReadingGoal(goalLength)
    }

    fun getGoalHeaderText(readingGoal: ReadingGoal): String {
        return when (readingGoal.goalLength) {
            "daily" -> "Daily Goal"
            "weekly" -> "Weekly Goal"
            "monthly" -> "Monthly Goal"
            "yearly" -> "Yearly Goal"
            else -> "Daily Goal"
        }
    }

    fun getGoalText(readingGoal: ReadingGoal): String {
        val goalProgress = readingGoal.goalProgress.toString()
        val goalNumber = readingGoal.goalNumber.toString()
        val goalUnits = readingGoal.goalType.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        return "$goalProgress / $goalNumber $goalUnits"
    }

    fun getProgressText(goalProgressPercent: Int): String {
        return "$goalProgressPercent%"
    }

    suspend fun getStatText(statName: String): String {
        return bookRepository.getStat(statName).statValue.toString()
    }

    fun getGoalProgress(readingGoal: ReadingGoal): Int {
        val doubleDiv = (readingGoal.goalProgress.toDouble() / readingGoal.goalNumber.toDouble()) * 100
        val intDiv = doubleDiv.roundToInt()
        return intDiv
    }

}