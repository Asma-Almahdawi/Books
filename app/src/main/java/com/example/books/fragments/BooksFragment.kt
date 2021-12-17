package com.example.books.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.books.Book
import com.example.books.R
import com.example.books.databinding.FragmentBooksBinding
import com.example.books.fragments.editfilefragment.REQUEST_CODE_IMAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.reflect.Field

private const val TAG = "BooksFragment"
private const val REQUEST_CODE_PDF = 1
class BooksFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
  val firestore= Firebase.firestore.collection("users")

   private lateinit var bindig :FragmentBooksBinding
   var cruPdfFile:Uri?= null

   val book = Book()

    private lateinit var viewModel: BooksViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       bindig= FragmentBooksBinding.inflate(layoutInflater)

        bindig.addBtn.setOnClickListener {
            val bookName = bindig.bookNameTv.text.toString()
            val autherName = bindig.autherNameTv.text.toString()
            val yearOfPublication = bindig.yearOfPublicationTv.text.toString()
            val pdfFile = bindig.filePDFBtn.text.toString()
            val book =Book(bookName,pdfFile,autherName,yearOfPublication)
            val firestorbook = Firebase.firestore.collection("books")
            firestorbook.add(book)
            firestore.document(auth.currentUser!!.uid).update("books",FieldValue.arrayUnion(book))

           Intent(Intent.ACTION_GET_CONTENT).also {
               it.type= "PDF/*"

               startActivityForResult(it, REQUEST_CODE_PDF)

           }


        }

        return bindig.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK && requestCode == REQUEST_CODE_PDF)

            data?.data.let {

                cruPdfFile= it
//                bindig.filePDFBtn.set

            }


    }

}