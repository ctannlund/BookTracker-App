package com.ctannlund.booklog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ctannlund.booklog.databinding.FragmentBookAddCustomBinding
import com.ctannlund.booklog.databinding.FragmentBookListBinding
import com.ctannlund.booklog.databinding.FragmentBookshelfBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class BookshelfFragment : Fragment() {

    private var _binding: FragmentBookshelfBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding null. Is view visible?"
        }
    private val bookshelfViewModel: BookshelfViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookshelfBinding.inflate(inflater, container, false)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.bookshelfRecyclerView.layoutManager = linearLayoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                bookshelfViewModel.books.collect { books ->
                    binding.bookshelfRecyclerView.adapter = BookshelfAdapter(books)
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}