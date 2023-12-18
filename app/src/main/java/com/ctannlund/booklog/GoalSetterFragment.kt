package com.ctannlund.booklog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ctannlund.booklog.databinding.FragmentGoalSetterBinding
import kotlinx.coroutines.launch

class GoalSetterFragment : Fragment() {

    private lateinit var yearGoal: ReadingGoal
    private lateinit var monthGoal: ReadingGoal
    private lateinit var weekGoal: ReadingGoal
    private lateinit var dayGoal: ReadingGoal
    private var _binding: FragmentGoalSetterBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding null. Is view visible?"
        }
    private val goalSetterViewModel: GoalSetterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        yearGoal = ReadingGoal(
            goalLength = "yearly",
            goalType = "books",
            goalNumber = 0,
            goalDeadline = goalSetterViewModel.calculateDeadline("yearly"),
            goalProgress = 0,
            goalSuccessCount = 0,
            goalCompleted = false
        )
        monthGoal = ReadingGoal(
            goalLength = "monthly",
            goalType = "books",
            goalNumber = 0,
            goalDeadline = goalSetterViewModel.calculateDeadline("monthly"),
            goalProgress = 0,
            goalSuccessCount = 0,
            goalCompleted = false
        )
        weekGoal = ReadingGoal(
            goalLength = "weekly",
            goalType = "pages",
            goalNumber = 0,
            goalDeadline = goalSetterViewModel.calculateDeadline("weekly"),
            goalProgress = 0,
            goalSuccessCount = 0,
            goalCompleted = false
        )
        dayGoal = ReadingGoal(
            goalLength = "daily",
            goalType = "pages",
            goalNumber = 0,
            goalDeadline = goalSetterViewModel.calculateDeadline("daily"),
            goalProgress = 0,
            goalSuccessCount = 0,
            goalCompleted = false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalSetterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            binding.goalBooksPerYear.doOnTextChanged{ number,_,_,_ ->
                try {
                    val yearNumber = number.toString().toInt()
                    yearGoal = yearGoal.copy(goalNumber = yearNumber)
                    // estimates
                    var monthBooks = yearNumber / 12
                    if (monthBooks < 1) {
                        monthBooks = 1
                    }
                    var yearPageEstimate = yearNumber * 350
                    if (yearPageEstimate < 1) {
                        yearPageEstimate = 1
                    }
                    var weekPageEstimate = yearPageEstimate / 52
                    if (weekPageEstimate < 1) {
                        weekPageEstimate = 1
                    }
                    var dayPageEstimate = yearPageEstimate / 365
                    if (dayPageEstimate < 1) {
                        dayPageEstimate = 1
                    }
                    monthGoal = monthGoal.copy(goalNumber = monthBooks)
                    weekGoal = weekGoal.copy(goalNumber = weekPageEstimate)
                    dayGoal = dayGoal.copy(goalNumber = dayPageEstimate)
                    binding.monthGoalEstimate.text = "$monthBooks Books / Month"
                    binding.weekGoalEstimate.text = "$weekPageEstimate Pages / Week"
                    binding.dayGoalEstimate.text = "$dayPageEstimate Pages / Day"
                }
                catch (_: Exception) {
                    binding.goalBooksPerYear.text.clear()
                }
            }

        }

        val saveButton: Button = view.findViewById(R.id.goal_finish_button)
        saveButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {

                if (yearGoal.goalNumber >= 1 && binding.goalBooksPerYear.text.isNotBlank()) {

                    goalSetterViewModel.addGoal(yearGoal)
                    goalSetterViewModel.addGoal(monthGoal)
                    goalSetterViewModel.addGoal(weekGoal)
                    goalSetterViewModel.addGoal(dayGoal)

                    findNavController().navigate(GoalSetterFragmentDirections.actionGoalSetterFragmentPop())
                }
                else {
                    binding.goalBooksPerYear.hint = "Invalid goal"
                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}