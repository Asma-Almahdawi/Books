package com.example.books.fragments.bookdetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.books.Book
import com.example.books.R
import com.example.books.databinding.EditFileFragmentBinding
import com.example.books.databinding.FragmentEditBookBinding
import com.example.books.fragments.AudioBookDetail.AudioBookDetailsFragmentArgs
import com.example.books.fragments.AudioBookDetail.AudioBookDetailsViewModel
import kotlinx.coroutines.launch

private const val TAG = "EditBookFragment"

class EditBookFragment : Fragment() {

    private val editBookViewModel by lazy { ViewModelProvider(this)[EditBookViewModel::class.java] }
    private lateinit var book: Book
    private lateinit var binding: FragmentEditBookBinding
    private val args: EditBookFragmentArgs by navArgs()
    lateinit var bookId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookId = args.bookId
        book = Book()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditBookBinding.inflate(layoutInflater)

        lifecycleScope.launch() {
            book = editBookViewModel.getBook(bookId) ?: Book()
            Log.d(TAG, "onCreateView: ")
            binding.bookNameTv.setText(book.bookName)
            Log.d(TAG, "onCreateView: ${book.bookName}")
            binding.autherNameTv.setText(book.bookOwner)
            binding.autherNameTv.setText(book.authorName)
            binding.yearOfPublicationTv.setText(book.yearOfPublication)

            binding.addBtn.setOnClickListener {

                editBookViewModel.updateBook(
                    book,
                    binding.bookNameTv.text.toString(),
                    binding.autherNameTv.text.toString(),
                    binding.yearOfPublicationTv.text.toString(),
                    )
            }
        }
        return binding.root
    }
}