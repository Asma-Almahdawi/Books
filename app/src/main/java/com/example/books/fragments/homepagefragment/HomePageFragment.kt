package com.example.books.fragments.homepagefragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.books.Book
import com.example.books.R
import com.example.books.database.AudioBook
import com.example.books.database.User
import com.example.books.databinding.BooksAddItemBinding
import com.example.books.databinding.HomePageFragmentBinding
import com.example.books.fragments.editfilefragment.EditFileViewModel
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "HomePageFragment"

class HomePageFragment : Fragment() {

    private val homePageViewModel by lazy { ViewModelProvider(this)[HomePageViewModel::class.java] }

    private lateinit var binding: HomePageFragmentBinding
    private lateinit var book: Book
    lateinit var bookId: String
    lateinit var audioBookId: String
    private lateinit var user: User
    private lateinit var audioBook: AudioBook


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomePageFragmentBinding.inflate(layoutInflater)
        user = User()
        if (homePageViewModel.getCurrentUserId().isNullOrEmpty()) {
            findNavController().navigate(R.id.action_navigation_home_to_loginFragment)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {

                    val search = newText.lowercase(Locale.getDefault())
                    lifecycleScope.launch {
                        homePageViewModel.searchBookName(search).observeForever { bookList ->
                            binding.booksRv.adapter = BookAdapter(bookList)
                        }
                    }
                }

                return true
            }


        })

        binding.booksRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.audioBookRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        book = Book()
        audioBook = AudioBook()
        lifecycleScope.launch {
            homePageViewModel.getAllAudioBook().observe(
                viewLifecycleOwner
            ) {
                binding.audioBookRv.adapter = AudioBookAdapter(it)
            }
        }

        lifecycleScope.launch {
            homePageViewModel.getAllBook().observe(viewLifecycleOwner, Observer {
                binding.booksRv.adapter = BookAdapter(it)
            })
        }

        return binding.root
    }

    //    --------------------------Book--------------------------------------//

    private inner class BookViewHolder(val binding: BooksAddItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var book: Book

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(book: Book) {
            this.book = book
            binding.bookNamesTv.text = book.bookName
            binding.bookImageTv.load(book.bookImage)
            bookId = book.bookId
            Log.d(TAG, "bind: ${book.bookImage}")
        }

        override fun onClick(v: View?) {
            when (v) {
                itemView -> {
                    val action =
                        HomePageFragmentDirections.actionNavigationHomeToBookDetailsFragment(book.bookId)
                    Log.d(TAG, "onClick: ${book.bookId}")
                    findNavController().navigate(action)
                }
            }
        }
    }

    private inner class BookAdapter(val books: List<Book>) :
        RecyclerView.Adapter<BookViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {

            val binding = BooksAddItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return BookViewHolder(binding)
        }

        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
            val book = books[position]
            holder.bind(book)
            Log.d("items", " this item $book")
        }

        override fun getItemCount(): Int {
            return books.size
        }
    }

    //    --------------------------AudioBook--------------------------------------//
    private inner class AudioBookViewHolder(val binding: BooksAddItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private lateinit var audioBook: AudioBook

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(audioBook: AudioBook) {
            this.audioBook = audioBook
            binding.bookNamesTv.text = audioBook.bookName
            binding.bookImageTv.load(audioBook.bookImage)
            audioBookId = audioBook.audioBookId
        }

        override fun onClick(v: View?) {
            when (v) {
                itemView -> {
                    val action =
                        HomePageFragmentDirections.actionNavigationHomeToAudioBookDetailsFragment(
                            audioBook.audioBookId
                        )
                    findNavController().navigate(action)
                }
            }

        }
    }

    private inner class AudioBookAdapter(val audioBooks: List<AudioBook>) :
        RecyclerView.Adapter<AudioBookViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AudioBookViewHolder {

            val binding = BooksAddItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )
            return AudioBookViewHolder(binding)

        }

        override fun onBindViewHolder(holder: AudioBookViewHolder, position: Int) {
            val audioBook = audioBooks[position]
            holder.bind(audioBook)
        }

        override fun getItemCount(): Int {
            return audioBooks.size
        }
    }
}