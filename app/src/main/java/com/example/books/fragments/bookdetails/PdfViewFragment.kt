package com.example.books.fragments.bookdetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.books.Book
import com.example.books.R
import com.example.books.databinding.FragmentPdfViewBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


private const val TAG = "PdfViewFragment"
class PdfViewFragment : Fragment() {
    private lateinit var binding: FragmentPdfViewBinding
    private val args: PdfViewFragmentArgs by navArgs()
    private lateinit var bookId:String
    private lateinit var book:Book
    companion object {
        fun newInstance() = PdfViewFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book=Book()

        bookId = args.bookId
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentPdfViewBinding.inflate(layoutInflater)

        loadPdfDetails()


        return binding.root
    }

    private fun loadPdfDetails() {
        Log.d(TAG, "loadPdfDetails: Get Pdf fromUrl db")

        val ref = Firebase.firestore.collection("books")
        ref.document(bookId)
            .get().addOnSuccessListener {
                val pdfUrl = it.get("pdfFile")

                loadBookFromUrl(pdfUrl as String)
            }
    }

    private fun loadBookFromUrl(pdfUrl: String) {
        var refr= Firebase.storage.getReferenceFromUrl(pdfUrl)
        refr.getBytes(50000000)
            .addOnSuccessListener {bytes->
                binding.pdfView.fromBytes(bytes)
                    .swipeHorizontal(false)
                    .onPageChange{page,pageCount->
                        Log.d(TAG, "loadBookFromUrl: ")

                    }.onError { t->
                        Log.d(TAG, "loadBookFromUrl: ${t.message}")
                    }.onPageError { page, t ->

                        Log.d(TAG, "loadBookFromUrl: ${t.message}")
                    }.load()

            }
    }


}