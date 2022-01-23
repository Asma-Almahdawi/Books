package com.example.books.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.books.Book
import com.example.books.R
import com.example.books.databinding.FragmentBooksBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private const val TAG = "BooksFragment"
private const val REQUEST_CODE_PDF = 1
private const val REQUEST_CODE_BOOK_IMAGE = 9
private const val REQUEST_CODE_BOOK_AUDIO = 0

class BooksFragment : Fragment() {

    private val booksViewModel by lazy { ViewModelProvider(this)[BooksViewModel::class.java] }
    private lateinit var auth: FirebaseAuth
    val firestore = Firebase.firestore.collection("users")
    private val imageBookRef = Firebase.storage.reference

    private lateinit var bindig: FragmentBooksBinding
    var cruPdfFile: Uri? = null

    var cruImage: Uri? = null
    var cruAudio: Uri? = null
    private val pdfRef = Firebase.storage.reference
    private lateinit var book: Book


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = Book()
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindig = FragmentBooksBinding.inflate(layoutInflater)

        bindig.addBtn.setOnClickListener {
            book.bookOwner = auth.currentUser!!.uid
            book.bookName = bindig.bookNameTv.text.toString()
            book.authorName = bindig.autherNameTv.text.toString()
            book.yearOfPublication = bindig.yearOfPublicationTv.text.toString()
            bindig.filePDFBtn.text = book.pdfFile

            if (book.bookName.isEmpty()) {
                Toast.makeText(context, "please enter the book name", Toast.LENGTH_SHORT).show()
            } else if (book.authorName.isEmpty()) {
                Toast.makeText(context, "please enter the author name", Toast.LENGTH_SHORT).show()
            } else if (book.yearOfPublication.isEmpty()) {
                Toast.makeText(context, "please enter the year", Toast.LENGTH_SHORT).show()
            } else if (book.bookImage.isEmpty()) {
                Toast.makeText(context, "please enter the book image", Toast.LENGTH_SHORT).show()
            } else if (book.pdfFile.isEmpty()) {

                Toast.makeText(context, "please enter the pdf file", Toast.LENGTH_SHORT).show()
            } else {
                booksViewModel.insertBook(book)
                findNavController().navigate(R.id.action_booksFragment_to_navigation_home)
            }

            firestore.document(auth.currentUser!!.uid).update("books", FieldValue.arrayUnion(book))
        }

        bindig.filePDFBtn.setOnClickListener {

            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "application/pdf"

                startActivityForResult(it, REQUEST_CODE_PDF)
            }
        }

        bindig.takePhoto.setOnClickListener {

            Intent(Intent.ACTION_GET_CONTENT).also {

                it.type = "image/*"

                startActivityForResult(it, REQUEST_CODE_BOOK_IMAGE)

            }
        }

        return bindig.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PDF) {
            data?.data.let {

                cruPdfFile = it

            }
            uploadPdfFile(book)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_BOOK_IMAGE) {

            data?.data.let {
                cruImage = it
                bindig.bookTv.setImageURI(it)
            }
            uploadImage(book)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_BOOK_AUDIO) {

            data?.data.let {
                cruAudio = it
            }
            uploadAudio()
        }
    }


    private fun uploadPdfFile(book: Book) = CoroutineScope(Dispatchers.IO).launch {
        try {
            cruPdfFile?.let {
                val ref = pdfRef.child("files/$book/${Calendar.getInstance().time}")
                val task = ref.putFile(it)
                val uriTask = task.continueWithTask { task ->

                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl
                }
                    .addOnSuccessListener {

                        val pdfFileUrl = it.toString()
                        book.pdfFile = pdfFileUrl
                        Log.d(TAG, "pdf file url $pdfFileUrl")
                        if (Firebase.auth.currentUser != null) {
                            Firebase.firestore.collection("books").document()
                                .update("pdfFile", book)
                        }

                    }.addOnFailureListener {
                        Log.d(TAG, "error url ")
                    }
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "true save", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage(book: Book) = CoroutineScope(Dispatchers.IO).launch {
        try {
            cruImage?.let {
                val ref =
                    imageBookRef.child("image/${this@BooksFragment.book}/${Calendar.getInstance().time}")
                val task = ref.putFile(it)
                val uriTask = task.continueWithTask { task ->

                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }

                    ref.downloadUrl
                }
                    .addOnSuccessListener {

                        val imageBookUrl = it.toString()
                        this@BooksFragment.book.bookImage = imageBookUrl
                        Log.d(TAG, "Image url $imageBookUrl")
                        if (Firebase.auth.currentUser != null) {
                            val userId = Firebase.auth.currentUser?.uid
                            Firebase.firestore.collection("books").document(userId!!).update(
                                "bookImage",
                                this@BooksFragment.book
                            )
                        }
                    }.addOnFailureListener {
                    }
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "true save", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadAudio() = CoroutineScope(Dispatchers.IO).launch {
        try {
            cruAudio?.let {
                val ref =
                    imageBookRef.child("audio/${this@BooksFragment.book}/${Calendar.getInstance().time}")
                val task = ref.putFile(it)

                val uriTask = task.continueWithTask { task ->

                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl
                }
                    .addOnSuccessListener {
                        val bookAudioUrl = it.toString()
                        this@BooksFragment.book.audioFile = bookAudioUrl
                        Log.d(TAG, "Image url $bookAudioUrl")
                        if (Firebase.auth.currentUser != null) {
                            val userId = Firebase.auth.currentUser?.uid
                            Firebase.firestore.collection("books").document(userId!!).update(
                                "audioFile",
                                book.audioFile
                            )
                        }
                    }.addOnFailureListener {

                    }
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "true save", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}