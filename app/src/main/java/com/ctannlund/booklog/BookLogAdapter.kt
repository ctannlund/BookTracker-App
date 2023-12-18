package com.ctannlund.booklog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ctannlund.booklog.databinding.ListItemLogBinding
import java.text.DateFormat

class LogHolder(
    private val binding: ListItemLogBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(log: Log) {
        val pageText = "${log.pages} Pages"
        binding.logBookTitle.text = log.title
        binding.logBookAuthor.text = log.author
        binding.logBookPages.text = pageText
        binding.logBookDate.text = DateFormat.getDateInstance(3).format(log.date)
    }

}

class LogListAdapter(private val logs: List<Log>) : RecyclerView.Adapter<LogHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemLogBinding.inflate(inflater, parent, false)
        return LogHolder(binding)
    }

    override fun getItemCount() = logs.size

    override fun onBindViewHolder(holder: LogHolder, position: Int) {
        val log = logs[position]
        holder.bind(log)
    }

}