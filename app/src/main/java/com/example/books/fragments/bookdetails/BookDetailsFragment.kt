package com.example.books.fragments.bookdetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.books.Book
import com.example.books.R
import com.example.books.databinding.FragmentBookDetailsBinding
import com.example.books.fragments.homepagefragment.HomePageFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BookDetailsFragment : Fragment() {

private lateinit var binding: FragmentBookDetailsBinding
private lateinit var book: Book
private lateinit var auth: FirebaseAuth
private val firebase = FirebaseAuth.getInstance()
//    val firestore = Firebase.firestore.collection("books").whereEqualTo("bookId" ,book.bookId)
//    private val args: BookDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = Book()
        auth = FirebaseAuth.getInstance()
//    val bookId = args.bookId

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentBookDetailsBinding.inflate(layoutInflater)
        getBookData()
        return binding.root
    }


    private fun getBookData(){
        val firestore = Firebase.firestore.collection("books").whereEqualTo("bookId" ,book.bookId)
        firestore.get().addOnSuccessListener {

            if (it != null){
                binding.imageBookTv.load(book.bookImage)
                
                binding.BookNameTv.setText(it.toString())
                binding.authorNameTv.setText(it.toString())
                binding.yearOfBookTv.setText(it.toString())



            }
        }



    }

}