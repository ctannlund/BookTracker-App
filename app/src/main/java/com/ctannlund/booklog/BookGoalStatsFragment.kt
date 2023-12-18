package com.ctannlund.booklog

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.ctannlund.booklog.databinding.FragmentGoalStatsBinding
import kotlinx.coroutines.launch

class BookGoalStatsFragment : Fragment() {

    private var _binding: FragmentGoalStatsBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding null. Is view visible?"
        }
    private val bookGoalStatsViewModel: BookGoalStatsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoalStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                try {
                    val goal = bookGoalStatsViewModel.getGoal("daily")
                    binding.statsGoalHeaderLabel.text = bookGoalStatsViewModel.getGoalHeaderText(goal)
                    binding.statsDailyGoal.text = bookGoalStatsViewModel.getGoalText(goal)
                    binding.statsProgressBarText.text = bookGoalStatsViewModel.getProgressText(bookGoalStatsViewModel.getGoalProgress(goal))
                    binding.statsProgressBar.progress = bookGoalStatsViewModel.getGoalProgress(goal)
                } catch (e: Exception) {
                    findNavController().navigate(BookGoalStatsFragmentDirections.actionStatsToGoalset())
                }

                try {
                    binding.pagesDayText.text = "Pages Read (Today): " + bookGoalStatsViewModel.getStatText("pagesDay")
                    binding.pagesWeekText.text = "Pages Read (This Week): " + bookGoalStatsViewModel.getStatText("pagesWeek")
                    binding.pagesMonthText.text = "Pages Read (This Month): " + bookGoalStatsViewModel.getStatText("pagesMonth")
                    binding.pagesYearText.text = "Pages Read (This Year): " + bookGoalStatsViewModel.getStatText("pagesYear")
                    binding.pagesLifeText.text = "Pages Read (Lifetime): " + bookGoalStatsViewModel.getStatText("pagesLifetime")

                    binding.booksDayText.text = "Books Finished (Today): " + bookGoalStatsViewModel.getStatText("booksDay")
                    binding.booksWeekText.text = "Books Finished (This Week): " + bookGoalStatsViewModel.getStatText("booksWeek")
                    binding.booksMonthText.text = "Books Finished (This Month): " + bookGoalStatsViewModel.getStatText("booksMonth")
                    binding.booksYearText.text = "Books Finished (This Year): " + bookGoalStatsViewModel.getStatText("booksYear")
                    binding.booksLifeText.text = "Books Finished (Lifetime): " + bookGoalStatsViewModel.getStatText("booksLifetime")

                    binding.dailyWinsText.text = "Daily Goals Completed: " + bookGoalStatsViewModel.getStatText("dailyGoalSuccess")
                    binding.weeklyWinsText.text = "Weekly Goals Completed: " + bookGoalStatsViewModel.getStatText("weeklyGoalSuccess")
                    binding.monthlyWinsText.text = "Monthly Goals Completed: " + bookGoalStatsViewModel.getStatText("monthlyGoalSuccess")
                    binding.yearlyWinsText.text = "Yearly Goals Completed: " + bookGoalStatsViewModel.getStatText("yearlyGoalSuccess")

                }
                catch (_: Exception) {
                    binding.pagesDayText.text = "Pages Read (Today): -"
                    binding.pagesWeekText.text = "Pages Read (This Week): -"
                    binding.pagesMonthText.text = "Pages Read (This Month): -"
                    binding.pagesYearText.text = "Pages Read (This Year): -"
                    binding.pagesLifeText.text = "Pages Read (Lifetime): -"
                    binding.booksDayText.text = "Books Finished (Today): -"
                    binding.booksWeekText.text = "Books Finished (This Week): -"
                    binding.booksMonthText.text = "Books Finished (This Month): -"
                    binding.booksYearText.text = "Books Finished (This Year): -"
                    binding.booksLifeText.text = "Books Finished (Lifetime): -"
                    binding.dailyWinsText.text = "Daily Goals Completed: -"
                    binding.weeklyWinsText.text = "Weekly Goals Completed: -"
                    binding.monthlyWinsText.text = "Monthly Goals Completed: -"
                    binding.yearlyWinsText.text = "Yearly Goals Completed: -"
                }
            }

        }

        val switchRight: ImageButton = view.findViewById(R.id.stats_goalswitcher_next_button)
        switchRight.setOnClickListener {
            bookGoalStatsViewModel.moveToNext()
            viewLifecycleOwner.lifecycleScope.launch {
                updateGoalUI(bookGoalStatsViewModel.getGoal(bookGoalStatsViewModel.currentHeaderLength))
            }
        }

        val switchLeft: ImageButton = view.findViewById(R.id.stats_goalswitcher_prev_button)
        switchLeft.setOnClickListener {
            bookGoalStatsViewModel.moveToPrev()
            viewLifecycleOwner.lifecycleScope.launch {
                updateGoalUI(bookGoalStatsViewModel.getGoal(bookGoalStatsViewModel.currentHeaderLength))
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateGoalUI(readingGoal: ReadingGoal) {
        binding.apply {
            statsGoalHeaderLabel.text = bookGoalStatsViewModel.getGoalHeaderText(readingGoal)
            statsDailyGoal.text = bookGoalStatsViewModel.getGoalText(readingGoal)
            statsProgressBarText.text = bookGoalStatsViewModel.getProgressText(bookGoalStatsViewModel.getGoalProgress(readingGoal))
            val goalProgress = bookGoalStatsViewModel.getGoalProgress(readingGoal)
            val progressAnimator = ObjectAnimator.ofInt(statsProgressBar, "progress", goalProgress)
            progressAnimator.duration = 60
            progressAnimator.start()
        }
    }

}