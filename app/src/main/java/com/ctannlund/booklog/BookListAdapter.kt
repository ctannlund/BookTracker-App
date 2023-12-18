package com.ctannlund.booklog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ctannlund.booklog.databinding.ListItemBookBinding
import java.util.UUID

class BookHolder(
    private val binding: ListItemBookBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(book: Book, onBookClicked: (bookTitle: String, bookAuthor: String, bookId: UUID) -> Unit) {
        val pageText = "${book.currentPages} / ${book.totalPages}"
        binding.bookTitle.text = book.title
        binding.bookAuthor.text = book.author
        binding.bookPages.text = pageText
        binding.root.setOnClickListener {
            onBookClicked(book.title, book.author, book.id)
        }
    }

}

class BookListAdapter(private val books: List<Book>,
    private val onBookClicked: (bookTitle: String, bookAuthor: String, bookId: UUID) -> Unit)
    : RecyclerView.Adapter<BookHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBookBinding.inflate(inflater, parent, false)
        return BookHolder(binding)
    }

    override fun getItemCount() = books.size

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val book = books[position]
        holder.bind(book, onBookClicked)
    }

}