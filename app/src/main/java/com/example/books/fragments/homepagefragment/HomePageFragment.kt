package com.example.books.fragments.homepagefragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.books.Book
import com.example.books.databinding.BooksAddItemBinding
import com.example.books.databinding.HomePageFragmentBinding
import com.example.books.fragments.editfilefragment.EditFileViewModel
import com.google.firebase.firestore.*
import kotlinx.coroutines.launch

private const val TAG = "HomePageFragment"
class HomePageFragment : Fragment() {

    private val homePageViewModel by lazy { ViewModelProvider(this)[HomePageViewModel::class.java] }

   private lateinit var binding: HomePageFragmentBinding
   private lateinit var book: Book
   private val bookList = mutableListOf<Book>()

   private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book=Book()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      binding= HomePageFragmentBinding.inflate(layoutInflater)
        binding.booksRv.layoutManager=LinearLayoutManager(context)
//        val book: List<Book> = listOf()
//        val bookAdapter = BookAdapter(book)
//        binding.booksRv.adapter=bookAdapter


//        binding.booksRv.adapter=BookAdapter(bookList)
        lifecycleScope.launch {

            homePageViewModel.getAllBook().observe(viewLifecycleOwner , Observer {
                binding.booksRv.adapter= BookAdapter(it)


            }

        ) }


        return binding.root
    }



//    .addOnSuccessListener {
//        for (doc in it){
//            val book = doc.toObject(Book::class.java)
//            bookList.add(book)
//
//
//        }
//
////            binding.booksRv.adapter=BookAdapter(bookList)
//
//    }



//    private fun updateUI(book: List<Book>) {
//        val bookAdapter = BookAdapter(book)
//        binding.booksRv.adapter=bookAdapter
//    }

    private inner class BookViewHolder(val binding: BooksAddItemBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(book: Book){
             binding.bookNamesTv.text=book.bookName
//            binding.bookImage.load(book.bookImage)
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
//            Log.d("Size" , "${books.size}")
        }


    }

    private fun fetchDataFromFirebase(){

        db.collection("books").get().addOnSuccessListener {
            for (doc in it){
                val book = doc.toObject(Book::class.java)
                bookList.add(book)
                Log.d(TAG," GET DATA $bookList")

        }
            binding.booksRv.adapter=BookAdapter(bookList)





        }


    }



}