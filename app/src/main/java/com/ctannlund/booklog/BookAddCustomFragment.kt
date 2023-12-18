package com.ctannlund.booklog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ctannlund.booklog.databinding.FragmentBookAddCustomBinding
import kotlinx.coroutines.launch
import java.util.*

class BookAddCustomFragment : Fragment() {

    private lateinit var book: Book
    private lateinit var bookshelfBook: BookshelfBook
    private var _binding: FragmentBookAddCustomBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Binding null. Is view visible?"
        }

    private val bookAddCustomViewModel: BookAddCustomViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idBook = UUID.randomUUID()
        book = Book(
            id = idBook,
            title = "Untitled",
            author = "Author Unknown",
            currentPages = 0,
            totalPages = 0,
            startDate = Date(),
            endDate = Date(),
            bookComplete = false
        )
        bookshelfBook = BookshelfBook(
            id = idBook,
            title = "Untitled",
            author = "Author Unknown",
            currentPages = 0,
            totalPages = 0,
            startDate = Date(),
            endDate = Date(),
            bookComplete = false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookAddCustomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bookTitle.doOnTextChanged { text,_,_,_ ->
                book = book.copy(title = text.toString())
                bookshelfBook = bookshelfBook.copy(title = text.toString())
            }
            bookAuthor.doOnTextChanged { text,_,_,_ ->
                book = book.copy(author = text.toString())
                bookshelfBook = bookshelfBook.copy(author = text.toString())
            }
            bookTotalPages.doOnTextChanged { number,_,_,_ ->
                try {
                    book = book.copy(totalPages = number.toString().toInt())
                }
                catch (_: Exception) {
                    binding.bookTotalPages.text.clear()
                }

            }

        }

        // in apply?

        val saveButton: Button = view.findViewById(R.id.book_add_save_button)
        saveButton.setOnClickListener {

            viewLifecycleOwner.lifecycleScope.launch {

                if (book.totalPages >= 1 && binding.bookTotalPages.text.isNotBlank()) {

                    bookAddCustomViewModel.addBook(book)
                    bookAddCustomViewModel.addBookshelfBook(bookshelfBook)
                    findNavController().navigate(
                        BookAddCustomFragmentDirections.actionBookAddCustomFragmentPop()
                    )
                }
                else {
                    binding.bookTotalPages.hint = "Invalid page count"
                }

            }

        }

        val cancelButton: Button = view.findViewById(R.id.book_add_cancel_button)
        cancelButton.setOnClickListener {
            val action = BookAddCustomFragmentDirections.actionBookAddCustomFragmentPop()
            view.findNavController().navigate(action)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}