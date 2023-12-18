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
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ctannlund.booklog.databinding.FragmentBookListBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class BookListFragment : Fragment() {

    private var _binding: FragmentBookListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding null. Is view visible?"
        }
    private val bookListViewModel: BookListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBookListBinding.inflate(inflater, container, false)
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {

            if (!bookListViewModel.checkGoalSetter()) {
                findNavController().navigate(BookListFragmentDirections.actionGoalSetter())
            }

            // initial UI state
            updateGoalUI(bookListViewModel.getReadingGoal("daily"))

        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                bookListViewModel.goalDaily.collect { goal ->
                    goal?.let { updateGoalUI(it) }
                }

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookListViewModel.books.collect { books ->
                    binding.bookRecyclerView.adapter = BookListAdapter(books) { bookTitle, bookAuthor, bookId ->
                        findNavController().navigate(BookListFragmentDirections.actionPageAdder(bookTitle, bookAuthor, bookId))
                    }
                }
            }

        }

        val floatButton: FloatingActionButton = view.findViewById(R.id.add_floating_button)
        floatButton.setOnClickListener {
            val action = BookListFragmentDirections.actionAddBook()
            view.findNavController().navigate(action)
        }

        val switchRight: ImageButton = view.findViewById(R.id.goalswitcher_next_button)
        switchRight.setOnClickListener {
            bookListViewModel.moveToNext()
            viewLifecycleOwner.lifecycleScope.launch {
                updateGoalUI(bookListViewModel.getReadingGoal(bookListViewModel.currentHeaderLength))
            }
        }

        val switchLeft: ImageButton = view.findViewById(R.id.goalswitcher_prev_button)
        switchLeft.setOnClickListener {
            bookListViewModel.moveToPrev()
            viewLifecycleOwner.lifecycleScope.launch {
                updateGoalUI(bookListViewModel.getReadingGoal(bookListViewModel.currentHeaderLength))
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateGoalUI(readingGoal: ReadingGoal) {
        binding.apply {
            goalHeaderLabel.text = bookListViewModel.getGoalHeaderText(readingGoal)
            booklistDailyGoal.text = bookListViewModel.getGoalText(readingGoal)
            val goalProgress = bookListViewModel.getGoalProgress(readingGoal)
            val progressAnimator = ObjectAnimator.ofInt(listProgressBar, "progress", goalProgress)
            progressAnimator.duration = 60
            progressAnimator.start()
        }
    }

}