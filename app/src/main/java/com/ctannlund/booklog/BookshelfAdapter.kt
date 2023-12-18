package com.ctannlund.booklog

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ctannlund.booklog.databinding.ListItemBookshelfBinding
import java.text.DateFormat

class BookshelfHolder(
    private val binding: ListItemBookshelfBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(bookshelfBook: BookshelfBook) {

        val pageText = "Started ${DateFormat.getDateInstance(3).format(bookshelfBook.startDate)}"

        val completeText = if (bookshelfBook.bookComplete) {
            "Completed ${DateFormat.getDateInstance(3).format(bookshelfBook.endDate)}"
        } else {
            "In Progress"
        }

        val coverColor = if (bookshelfBook.bookComplete) {
            "#b5a228"
        } else {
            "#9F7833"
        }

        binding.bookshelfBookTitle.text = bookshelfBook.title
        binding.bookshelfBookAuthor.text = bookshelfBook.author
        binding.bookshelfComplete.text = completeText
        binding.bookshelfStarted.text = pageText
        binding.bookshelfItemCover.setColorFilter(Color.parseColor(coverColor))
    }

}

class BookshelfAdapter(private val bookshelfBooks: List<BookshelfBook>)
    : RecyclerView.Adapter<BookshelfHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookshelfHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBookshelfBinding.inflate(inflater, parent, false)
        return BookshelfHolder(binding)
    }

    override fun getItemCount() = bookshelfBooks.size

    override fun onBindViewHolder(holder: BookshelfHolder, position: Int) {
        val book = bookshelfBooks[position]
        holder.bind(book)
    }

}