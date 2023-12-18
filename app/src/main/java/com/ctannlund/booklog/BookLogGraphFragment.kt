package com.ctannlund.booklog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ctannlund.booklog.databinding.FragmentLogsGraphBinding
import kotlinx.coroutines.launch

class BookLogGraphFragment : Fragment() {
    private var _binding: FragmentLogsGraphBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding null. Is view visible?"
        }
    private val logListViewModel: BookLogGraphViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogsGraphBinding.inflate(inflater, container, false)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        binding.logsRecyclerView.layoutManager = linearLayoutManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                logListViewModel.logs.collect { logs ->
                    binding.logsRecyclerView.adapter = LogListAdapter(logs)
                }

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // WIP
//    fun configChart() {
//        val desc = Description()
//        desc.text = "Reading History"
//        desc.textSize = 12F
//        binding.logLinechart.description = desc
//
//        val xAxis = binding.logLinechart.xAxis
//
//        xAxis.valueFormatter = valFormat
//    }

}