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
import com.example.books.fragments.editfilefragment.REQUEST_CODE_IMAGE
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
private const val  REQUEST_CODE_IMAGEE =9
class BooksFragment : Fragment() {

    private val booksViewModel by lazy { ViewModelProvider(this)[BooksViewModel::class.java] }
    private lateinit var auth: FirebaseAuth
  val firestore= Firebase.firestore.collection("users")
    val bookDoc = Firebase.firestore.collection("books")
    private val imageBookRef = Firebase.storage.reference

   private lateinit var bindig :FragmentBooksBinding
   var cruPdfFile:Uri?= null

    var cruImage :Uri? = null
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

       bindig= FragmentBooksBinding.inflate(layoutInflater)

        bindig.addBtn.setOnClickListener {
//            val bookOwner = bindig.bookownerTv.text.toString()
            book.bookOwner= auth.currentUser!!.uid
            book.bookName= bindig.bookNameTv.text.toString()
//            book.pdfFile=bindig.filePDFBtn.text.toString()
            book.authorName=bindig.autherNameTv.text.toString()
            book.yearOfPublication= bindig.yearOfPublicationTv.text.toString()
               bindig.filePDFBtn.text = book.pdfFile



//            val book = Book( bookImage ,bookOwner=auth.currentUser!!.uid,bookName,pdfFile,autherName,yearOfPublication )
            booksViewModel.insertBook(book)


            firestore.document(auth.currentUser!!.uid).update("books",FieldValue.arrayUnion(book))
//            uploadPdfFile(book)


findNavController().navigate(R.id.action_booksFragment_to_navigation_home)

        }

        bindig.filePDFBtn.setOnClickListener {

            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type= "application/pdf"

                startActivityForResult(it, REQUEST_CODE_PDF)

            }
        }

        bindig.takePhoto.setOnClickListener {

            Intent(Intent.ACTION_GET_CONTENT).also {

                it.type = "image/*"

                startActivityForResult(it, REQUEST_CODE_IMAGEE)

            }


        }

        return bindig.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK && requestCode == REQUEST_CODE_PDF){
            data?.data.let {

                cruPdfFile= it
//                bindig.filePDFBtn.set

            }
            uploadPdfFile(book)
        }
        if (resultCode==Activity.RESULT_OK && requestCode ==REQUEST_CODE_IMAGEE){

            data?.data.let {
                cruImage = it
                bindig.bookTv.setImageURI(it)
            }
            uploadImage(book)
        }



        }






    private fun uploadPdfFile(book: Book)= CoroutineScope(Dispatchers.IO).launch {
        try {
            cruPdfFile?.let {
                val ref =  pdfRef.child("files/$book/${Calendar.getInstance().time}")
                val task =ref.putFile(it)

                val uriTask = task.continueWithTask{task ->

                    if (!task.isSuccessful){
                        task.exception?.let {
                            throw it
                        }

                    }

                    ref.downloadUrl
                }
                    .addOnSuccessListener {

                        val pdfFileUrl = it.toString()
                        book.pdfFile= pdfFileUrl
                        Log.d(TAG, "pdf file url $pdfFileUrl")
//                  Firebase.firestore.collection("users").document(Firebase.auth.currentUser?.uid!!).set(
//                      hashMapOf("imageUrl" to imageUrl))
                        if(Firebase.auth.currentUser != null){
//                            val userId = UUID.randomUUID()
//                            Firebase.firestore.collection("books").document().set(book,
//                                SetOptions.merge())
                            Firebase.firestore.collection("books").document().update("pdfFile" , book)
                        }

//                      .update("profileImageUrl",imageUrl)


                    }.addOnFailureListener {
                        Log.d(TAG,"error url ")
                    }
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"true save", Toast.LENGTH_SHORT).show()
                }
            }}
        catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun uploadImage(book: Book) = CoroutineScope(Dispatchers.IO).launch {
        try {
            cruImage?.let {
                val ref =  imageBookRef.child("image/${this@BooksFragment.book}/${Calendar.getInstance().time}")
                val task =ref.putFile(it)

                val uriTask = task.continueWithTask{task ->

                    if (!task.isSuccessful){
                        task.exception?.let {
                            throw it
                        }

                    }

                    ref.downloadUrl
                }
                    .addOnSuccessListener {

                        val imageBookUrl = it.toString()
                        this@BooksFragment.book.bookImage = imageBookUrl
                        Log.d(TAG,"Image url $imageBookUrl")

//                  Firebase.firestore.collection("users").document(Firebase.auth.currentUser?.uid!!).set(
//                      hashMapOf("imageUrl" to imageUrl))
                        if(Firebase.auth.currentUser != null){
                            val userId = Firebase.auth.currentUser?.uid
                            Firebase.firestore.collection("books").document(userId!!).update("bookImage",
                                this@BooksFragment.book
                            )
                        }

//                      .update("profileImageUrl",imageUrl)


                    }.addOnFailureListener {
//                        Log.d(com.example.books.fragments.editfilefragment.TAG,"error url ")
                    }
                withContext(Dispatchers.Main){
                    Toast.makeText(context,"true save",Toast.LENGTH_SHORT).show()
                }
            }}
        catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun saveUserProfileUrl(){

        val uid= FirebaseAuth.getInstance().uid





    }
}