package com.ctannlund.booklog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ctannlund.booklog.databinding.FragmentPageUpdaterBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.*

class PageAdderFragment : Fragment() {

    private lateinit var pagesThisDay: Statistic
    private lateinit var pagesThisWeek: Statistic
    private lateinit var pagesThisMonth: Statistic
    private lateinit var pagesThisYear: Statistic
    private lateinit var pagesLifetime: Statistic
    private lateinit var booksThisDay: Statistic
    private lateinit var booksThisWeek: Statistic
    private lateinit var booksThisMonth: Statistic
    private lateinit var booksThisYear: Statistic
    private lateinit var booksLifetime: Statistic
    private lateinit var dailyWins: Statistic
    private lateinit var weeklyWins: Statistic
    private lateinit var monthlyWins: Statistic
    private lateinit var yearlyWins: Statistic
    private lateinit var readingLog: Log

    private val minWheel = 0
    private val maxWheel = 9999

    private val regularStats = arrayOf("pagesDay", "pagesWeek", "pagesMonth", "pagesYear", "pagesLifetime")
    private val completedStats = arrayOf("booksDay", "booksWeek", "booksMonth", "booksYear", "booksLifetime")

    private var _binding: FragmentPageUpdaterBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding null. Is view visible?"
        }

    private val args: PageAdderFragmentArgs by navArgs()

    private val pageAdderViewModel: PageAdderViewModel by viewModels {
        PageAdderViewModelFactory(args.bookId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pagesThisDay = Statistic(
            statName = "pagesDay",
            statValue = 0
        )
        pagesThisWeek = Statistic(
            statName = "pagesWeek",
            statValue = 0
        )
        pagesThisMonth = Statistic(
            statName = "pagesMonth",
            statValue = 0
        )
        pagesThisYear = Statistic(
            statName = "pagesYear",
            statValue = 0
        )
        pagesLifetime = Statistic(
            statName = "pagesLifetime",
            statValue = 0
        )
        booksThisDay = Statistic(
            statName = "booksDay",
            statValue = 0
        )
        booksThisWeek = Statistic(
            statName = "booksWeek",
            statValue = 0
        )
        booksThisMonth = Statistic(
            statName = "booksMonth",
            statValue = 0
        )
        booksThisYear = Statistic(
            statName = "booksYear",
            statValue = 0
        )
        booksLifetime = Statistic(
            statName = "booksLifetime",
            statValue = 0
        )
        dailyWins = Statistic(
            statName = "dailyGoalSuccess",
            statValue = 0
        )
        weeklyWins = Statistic(
            statName = "weeklyGoalSuccess",
            statValue = 0
        )
        monthlyWins = Statistic(
            statName = "monthlyGoalSuccess",
            statValue = 0
        )
        yearlyWins = Statistic(
            statName = "yearlyGoalSuccess",
            statValue = 0
        )
        readingLog = Log(
            id = UUID.randomUUID(),
            title = "",
            author = "",
            pages = 0,
            date = Date()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPageUpdaterBinding.inflate(inflater, container, false)

        setupNumberPicker()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            pageAdderTitle.doOnTextChanged { text, _, _, _ ->
                pageAdderViewModel.updateBook { prevBook ->
                    prevBook.copy(title = text.toString())
                }
                readingLog = readingLog.copy(title = text.toString())
            }

            pageAdderAuthor.doOnTextChanged { text, _, _, _ ->
                pageAdderViewModel.updateBook { prevBook ->
                    prevBook.copy(author = text.toString())
                }
                readingLog = readingLog.copy(author = text.toString())
            }

            pageNumberPicker.setOnValueChangedListener {numberPicker, i, i2 ->
                binding.readCountText.text = "Pages Read: ${i2 - numberPicker.minValue}"
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {

            binding.pageNumberPicker.maxValue = pageAdderViewModel.getPageMaxCount(args.bookId)
            binding.pageNumberPicker.minValue = pageAdderViewModel.getPageMinCount(args.bookId)

            try {
                pageAdderViewModel.getStat("pagesWeek").statValue
            }
            catch (_: Exception) {
                // replace all this with loops or do something better entirely
                pageAdderViewModel.addStat(pagesThisDay)
                pageAdderViewModel.addStat(pagesThisWeek)
                pageAdderViewModel.addStat(pagesThisMonth)
                pageAdderViewModel.addStat(pagesThisYear)
                pageAdderViewModel.addStat(pagesLifetime)
                pageAdderViewModel.addStat(booksThisDay)
                pageAdderViewModel.addStat(booksThisWeek)
                pageAdderViewModel.addStat(booksThisMonth)
                pageAdderViewModel.addStat(booksThisYear)
                pageAdderViewModel.addStat(booksLifetime)
                pageAdderViewModel.addStat(dailyWins)
                pageAdderViewModel.addStat(weeklyWins)
                pageAdderViewModel.addStat(monthlyWins)
                pageAdderViewModel.addStat(yearlyWins)
            }

            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                pageAdderViewModel.book.collect { book ->
                    book?.let { updateUI(it) }
                }
            }
        }


        val saveButton: Button = view.findViewById(R.id.book_page_save_button)
        saveButton.setOnClickListener {

            val pageWheelNumber = view.findViewById<NumberPicker>(R.id.page_number_picker).value

            val totalPagesRead = pageWheelNumber - binding.pageNumberPicker.minValue

            viewLifecycleOwner.lifecycleScope.launch {

                readingLog = readingLog.copy(pages = totalPagesRead)
                readingLog = readingLog.copy(date = Date())
                pageAdderViewModel.addLog(readingLog)

                // set book's pages

                pageAdderViewModel.updateBook { prevBook ->
                    prevBook.copy(currentPages = pageWheelNumber)
                }

                // update page-based reading goals

                pageAdderViewModel.updateReadingGoal(pageAdderViewModel._goalDaily) { prevGoal ->
                    prevGoal.copy(goalProgress = (prevGoal.goalProgress + totalPagesRead))
                }

                pageAdderViewModel.updateReadingGoal(pageAdderViewModel._goalWeekly) { prevGoal ->
                    prevGoal.copy(goalProgress = (prevGoal.goalProgress + totalPagesRead))
                }

                // update regular stats

                for (stat in regularStats) {

                    val currentStat : MutableStateFlow<Statistic?> = MutableStateFlow(pageAdderViewModel.getStat(stat))
                    currentStat.value = pageAdderViewModel.getStat(stat)

                    pageAdderViewModel.updateStat(currentStat) { prevStat ->
                        prevStat.copy(statValue = (prevStat.statValue + totalPagesRead))
                    }

                }

                // update completed book stats (if completed)

                if (pageAdderViewModel.checkBookComplete(pageAdderViewModel.getBook(args.bookId), pageWheelNumber)) {

                    pageAdderViewModel.updateBook { prevBook ->
                        prevBook.copy(currentPages = prevBook.totalPages)
                    }

                    for (stat in completedStats) {

                        val currentStat : MutableStateFlow<Statistic?> = MutableStateFlow(pageAdderViewModel.getStat(stat))
                        currentStat.value = pageAdderViewModel.getStat(stat)

                        pageAdderViewModel.updateStat(currentStat) { prevStat ->
                            prevStat.copy(statValue = (prevStat.statValue + 1))
                        }

                    }

                    // update book goals

                    pageAdderViewModel.updateReadingGoal(pageAdderViewModel._goalMonthly) { prevGoal ->
                        prevGoal.copy(goalProgress = (prevGoal.goalProgress + 1))
                    }
                    pageAdderViewModel.updateReadingGoal(pageAdderViewModel._goalYearly) { prevGoal ->
                        prevGoal.copy(goalProgress = (prevGoal.goalProgress + 1))
                    }

                    // complete bookshelf copy / clear the book from main list once done
                    val completeDate = DateTime().toDate()
                    pageAdderViewModel.updateBookshelfBook { prevBook ->
                        prevBook.copy(bookComplete = true, endDate = completeDate)
                    }

                    val bookToDelete = pageAdderViewModel.getBook(args.bookId)
                    pageAdderViewModel.deleteBook(bookToDelete)

                    Toast.makeText(requireContext(), "Book Completed!",
                        Toast.LENGTH_SHORT).show()

                }

                findNavController().navigate(
                    PageAdderFragmentDirections.actionPageAdderFragmentPop()
                )
            }

        }

        val cancelButton: Button = view.findViewById(R.id.book_page_cancel_button)
        cancelButton.setOnClickListener {
            val action = PageAdderFragmentDirections.actionPageAdderFragmentPop()
            view.findNavController().navigate(action)
        }

        var delClicked = false
        val deleteButton: Button = view.findViewById(R.id.book_delete_button)
        deleteButton.setOnClickListener {
            delClicked = if(!delClicked) {
                Toast.makeText(requireContext(), "Press again to confirm deletion",
                    Toast.LENGTH_SHORT).show()
                true
            } else {
                Toast.makeText(requireContext(), "Book deleted",
                    Toast.LENGTH_SHORT).show()
                viewLifecycleOwner.lifecycleScope.launch {
                    val bookToDelete = pageAdderViewModel.getBook(args.bookId)
                    pageAdderViewModel.deleteBook(bookToDelete)
                    val bookshelfToDelete = pageAdderViewModel.getBookshelfBook(args.bookId)
                    pageAdderViewModel.deleteBookshelfBook(bookshelfToDelete)
                    val action = PageAdderFragmentDirections.actionPageAdderFragmentPop()
                    view.findNavController().navigate(action)
                }
                false
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupNumberPicker() {
        val numberPicker = binding.pageNumberPicker
        numberPicker.minValue = minWheel
        numberPicker.maxValue = maxWheel
        numberPicker.wrapSelectorWheel = true
    }

    private fun updateUI(book: Book) {
        binding.apply {
            if (pageAdderTitle.text.toString() != book.title) {
                pageAdderTitle.text = book.title
            }
            if (pageAdderAuthor.text.toString() != book.author) {
                pageAdderAuthor.text = book.author
            }
        }
    }

}