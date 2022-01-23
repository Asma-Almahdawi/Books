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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.books.Book
import com.example.books.R
import com.example.books.database.AudioBook
import com.example.books.database.Favorite
import com.example.books.databinding.FragmentAudioBookkBinding
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

private const val TAG = "AudioBookFragment"
private const val REQUEST_CODE_BOOK_IMAGE = 9
private const val REQUEST_CODE_BOOK_AUDIO = 0

class AudioBookFragment : Fragment() {


    private val audioBookViewModel by lazy { ViewModelProvider(this)[AudioBookViewModel::class.java] }
    private lateinit var auth: FirebaseAuth
    val firestore = Firebase.firestore.collection("users")
    private val imageBookRef = Firebase.storage.reference
    private lateinit var bindig: FragmentAudioBookkBinding
    var cruImage: Uri? = null
    var cruAudio: Uri? = null
    private lateinit var audioBook: AudioBook


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioBook = AudioBook()
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindig = FragmentAudioBookkBinding.inflate(layoutInflater)

        bindig.addBtn.setOnClickListener {
            audioBook.bookOwner = auth.currentUser!!.uid
            audioBook.bookName = bindig.bookNameTv.text.toString()
            audioBook.authorName = bindig.autherNameTv.text.toString()
            bindig.audioBtn.text = audioBook.audioFile
            audioBook.yearOfPublication = bindig.yearOfPublicationTv.text.toString()

            if (audioBook.bookName.isEmpty()) {
                Toast.makeText(context, "please enter the book name", Toast.LENGTH_SHORT).show()
            } else if (audioBook.authorName.isEmpty()) {
                Toast.makeText(context, "please enter the author name", Toast.LENGTH_SHORT).show()
            } else if (audioBook.yearOfPublication.isEmpty()) {
                Toast.makeText(context, "please enter the year", Toast.LENGTH_SHORT).show()
            } else if (audioBook.bookImage.isEmpty()) {
                Toast.makeText(context, "please enter the book image", Toast.LENGTH_SHORT).show()
            } else {
                audioBookViewModel.insertAudioBook(audioBook)
                findNavController().navigate(R.id.action_audioBookFragment_to_navigation_home)
            }

            firestore.document(auth.currentUser!!.uid)
                .update("audioBooks", FieldValue.arrayUnion(audioBook))
        }

        bindig.takePhoto.setOnClickListener {

            Intent(Intent.ACTION_GET_CONTENT).also {

                it.type = "image/*"

                startActivityForResult(it, REQUEST_CODE_BOOK_IMAGE)
            }
        }

        bindig.audioBtn.setOnClickListener {

            Intent(Intent.ACTION_GET_CONTENT).also {

                it.type = "audio/*"

                startActivityForResult(it, REQUEST_CODE_BOOK_AUDIO)
            }
        }

        return bindig.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_BOOK_IMAGE) {

            data?.data.let {
                cruImage = it
                bindig.bookTv.setImageURI(it)
            }
            uploadImage()
        }
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_BOOK_AUDIO) {

            data?.data.let {
                cruAudio = it
            }
            uploadAudio()
        }
    }


    private fun uploadImage() = CoroutineScope(Dispatchers.IO).launch {
        try {
            cruImage?.let {
                val ref = imageBookRef.child("image/${this}/${Calendar.getInstance().time}")
                val task = ref.putFile(it)
                val uriTask = task.continueWithTask { task ->

                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl

                }.addOnSuccessListener {
                        val imageBookUrl = it.toString()
                        audioBook.bookImage = imageBookUrl
                        Log.d(TAG, "Image url $imageBookUrl")
                        if (Firebase.auth.currentUser != null) {
                            val userId = Firebase.auth.currentUser?.uid
                            Firebase.firestore.collection("books").document(userId!!).update(
                                "bookImage",
                                audioBook.bookImage
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
                val ref = imageBookRef.child("audio//${Calendar.getInstance().time}")
                val task = ref.putFile(it)

                val uriTask = task.continueWithTask { task ->

                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl

                }.addOnSuccessListener {

                        val bookAudioUrl = it.toString()
                        audioBook.audioFile = bookAudioUrl
                        Log.d(TAG, "Audio url $bookAudioUrl")
                        Log.d(TAG, "Audio url ${audioBook}")
                        if (Firebase.auth.currentUser != null) {
                            val userId = Firebase.auth.currentUser?.uid
                            Firebase.firestore.collection("audioBooks").document(userId!!).update(
                                "audioFile",
                                audioBook.audioFile
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