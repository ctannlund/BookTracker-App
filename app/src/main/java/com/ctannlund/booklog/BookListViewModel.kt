package com.ctannlund.booklog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.*
import kotlin.math.roundToInt

class BookListViewModel : ViewModel() {

    private val bookRepository = BookRepository.get()

    private val _books: MutableStateFlow<List<Book>> = MutableStateFlow(emptyList())
    val books: StateFlow<List<Book>>
        get() = _books.asStateFlow()

    private val _goalDaily:MutableStateFlow<ReadingGoal?> = MutableStateFlow(null)
    val goalDaily:StateFlow<ReadingGoal?> = _goalDaily.asStateFlow()

    private val _goalWeekly:MutableStateFlow<ReadingGoal?> = MutableStateFlow(null)

    private val _goalMonthly:MutableStateFlow<ReadingGoal?> = MutableStateFlow(null)

    private val _goalYearly:MutableStateFlow<ReadingGoal?> = MutableStateFlow(null)

    private val goalsArray = arrayOf("daily", "weekly", "monthly", "yearly")
    private val goalsFArray = arrayOf(_goalDaily, _goalWeekly, _goalMonthly, _goalYearly)

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

    private val dailyStats = arrayOf("pagesDay", "booksDay")
    private val weeklyStats = arrayOf("pagesWeek", "booksWeek")
    private val monthlyStats = arrayOf("pagesMonth", "booksMonth")
    private val yearlyStats = arrayOf("pagesYear", "booksYear")

    private val statsArray = arrayOf(dailyStats, weeklyStats, monthlyStats, yearlyStats)
    private val successStatsArray = arrayOf("dailyGoalSuccess", "weeklyGoalSuccess",
        "monthlyGoalSuccess", "yearlyGoalSuccess")

    init {
        viewModelScope.launch {

            _goalDaily.value = bookRepository.getReadingGoal("daily")
            _goalWeekly.value = bookRepository.getReadingGoal("weekly")
            _goalMonthly.value = bookRepository.getReadingGoal("monthly")
            _goalYearly.value = bookRepository.getReadingGoal("yearly")

            for (goal in goalsArray) {

                if (passedGoalDeadline(bookRepository.getReadingGoal(goal))) {

                    val newGoalDeadline = calculateDeadline(goal)

                    var goalSuccess = 0
                    if (getGoalProgress(bookRepository.getReadingGoal(goal)) >= 100) {
                        goalSuccess = 1
                    }

                    // reset reading goals

                    updateReadingGoal(goalsFArray[goalsArray.indexOf(goal)]) { prevGoal ->
                        prevGoal.copy(
                            goalProgress = 0,
                            goalSuccessCount = (prevGoal.goalSuccessCount + goalSuccess),
                            goalDeadline = newGoalDeadline
                        )
                    }

                    // reset reading goal time-based stats

                    goalsFArray[goalsArray.indexOf(goal)].value?.let { bookRepository.updateReadingGoal(it) }

                    for (stat in statsArray[goalsArray.indexOf(goal)]) {

                        val currentStat : MutableStateFlow<Statistic?> = MutableStateFlow(bookRepository.getStat(stat))
                        currentStat.value = bookRepository.getStat(stat)
                        val statUpdate:StateFlow<Statistic?> = currentStat.asStateFlow()

                        updateStat(currentStat) { prevStat ->
                            prevStat.copy(statValue = 0)
                        }

                        statUpdate.value?.let { bookRepository.updateStat(it) }

                    }

                    // add to the respective daily goal win

                    val goalSuccessStat = successStatsArray[goalsArray.indexOf(goal)]
                    val currentSuccessStat : MutableStateFlow<Statistic?> = MutableStateFlow(bookRepository.getStat(goalSuccessStat))
                    currentSuccessStat.value = bookRepository.getStat(goalSuccessStat)
                    val successStatUpdate:StateFlow<Statistic?> = currentSuccessStat.asStateFlow()

                    updateStat(currentSuccessStat) { prevStat ->
                        prevStat.copy(statValue = (prevStat.statValue + goalSuccess))
                    }

                    successStatUpdate.value?.let { bookRepository.updateStat(it) }

                }

            }

            bookRepository.getBooks().collect {
                _books.value = it
            }

        }

    }

    private fun updateStat(statNum: MutableStateFlow<Statistic?>, onUpdate: (Statistic) -> Statistic) {
        statNum.update { prevStat ->
            prevStat?.let { onUpdate(it) }
        }
    }

    suspend fun getReadingGoal(goalLength: String): ReadingGoal {
        return bookRepository.getReadingGoal(goalLength)
    }

    suspend fun checkGoalSetter(): Boolean {
        return try {
            getReadingGoal("daily").goalType == "pages"
        }
        catch (_: Exception) {
            false
        }
    }

    private fun updateReadingGoal(goalNum: MutableStateFlow<ReadingGoal?>, onUpdate: (ReadingGoal) -> ReadingGoal) {
        goalNum.update { prevGoal ->
            prevGoal?.let { onUpdate(it) }
        }
    }

    private fun calculateDeadline(goalLength: String): Date {

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

    private fun passedGoalDeadline(readingGoal: ReadingGoal): Boolean {

        return try {
            val goalDeadline = DateTime(readingGoal.goalDeadline).toDateTime()
            DateTime().isAfter(goalDeadline)
        }catch (_: Exception) {
            false
        }

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

    fun getGoalProgress(readingGoal: ReadingGoal): Int {
        val doubleDiv =
            (readingGoal.goalProgress.toDouble() / readingGoal.goalNumber.toDouble()) * 100
        return doubleDiv.roundToInt()
    }

}