package com.example.books.fragments.homepagefragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.books.Book
import com.example.books.R
import com.example.books.databinding.BooksAddItemBinding
import com.example.books.databinding.HomePageFragmentBinding
import com.example.books.fragments.editfilefragment.EditFileViewModel
import com.google.android.gms.auth.api.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.launch

private const val TAG = "HomePageFragment"
class HomePageFragment : Fragment() {

    private val homePageViewModel by lazy { ViewModelProvider(this)[HomePageViewModel::class.java] }

   private lateinit var binding: HomePageFragmentBinding
   private lateinit var book: Book
   private val bookList = mutableListOf<Book>()
    lateinit var bookId:String




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      binding= HomePageFragmentBinding.inflate(layoutInflater)

        if (homePageViewModel.getCurrentUserId().isNullOrEmpty()){

            findNavController().navigate(R.id.action_navigation_home_to_loginFragment)


        }


        binding.booksRv.layoutManager=LinearLayoutManager(context , LinearLayoutManager.HORIZONTAL,false)
        book=Book()

        lifecycleScope.launch {

            homePageViewModel.getAllBook().observe(viewLifecycleOwner , Observer {
                binding.booksRv.adapter= BookAdapter(it)
            }

            ) }


        return binding.root
    }





    private inner class BookViewHolder(val binding: BooksAddItemBinding):RecyclerView.ViewHolder(binding.root),View.OnClickListener{
            private lateinit var book: Book
        init {
            binding.bookDelete.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }
        fun bind(book: Book){
            this.book = book
             binding.bookNamesTv.text=book.bookName
            binding.bookImageTv.load(book.bookImage)
            bookId = book.bookId
            Log.d(TAG, "bind: ${book.bookImage}")
        }

        override fun onClick(v: View?) {
            when(v){
                binding.bookDelete -> {
                    if (homePageViewModel.getCurrentUserId()== book.bookOwner) {
                        homePageViewModel.deleteBook(book)
                    }
                }
                itemView->{
                    val action = HomePageFragmentDirections.actionNavigationHomeToBookDetailsFragment(book.bookId)
                    Log.d(TAG, "onClick: ${book.bookId}")
                    findNavController().navigate(action)
                }
            }

        }




    }

    private inner class BookAdapter(val books:List<Book>): RecyclerView.Adapter<BookViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {

            val binding=BooksAddItemBinding.inflate(

                layoutInflater,
                parent,
                false

            )
            return BookViewHolder(binding)

        }


        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
         val book =books[position]

            holder.bind(book)
            Log.d("items", " this item $book")
        }

        override fun getItemCount(): Int {
            return books.size

        }


    }




}