package com.example.books.fragments.bookdetails

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.books.Book
import com.example.books.R
import com.example.books.commentFragment.Comment
import com.example.books.commentFragment.UserComment
import com.example.books.database.Favorite
import com.example.books.database.RatingBook
import com.example.books.database.User
import com.example.books.databinding.CommentListItemBinding
import com.example.books.databinding.FragmentBookDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.util.*


private const val TAG = "BookDetailsFragment"

class BookDetailsFragment : Fragment() {

    private val bookDetailsViewModel by lazy { ViewModelProvider(this)[BookDetailsViewModel::class.java] }

    private lateinit var binding: FragmentBookDetailsBinding
    private lateinit var book: Book
    private lateinit var user: User
    private lateinit var auth: FirebaseAuth
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var favorite: Favorite

    var ratingAverage = 0f
    private val args: BookDetailsFragmentArgs by navArgs()
    lateinit var bookId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        mediaPlayer = MediaPlayer()
        favorite = Favorite()
        user = User()
        bookId = args.bookId
        Log.d(TAG, "onCreate: $bookId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBookDetailsBinding.inflate(layoutInflater)

      lifecycleScope.launch() {
            book = bookDetailsViewModel.getBook(bookId) ?: Book()
            Log.d(TAG, "onCreateView: ")
            binding.BookNameTv.setText(book.bookName)
            Log.d(TAG, "onCreateView: ${book.bookName}")
            binding.authorNameTv.setText(book.bookOwner)
            binding.authorNameTv.setText(book.authorName)
            binding.imageBookTv.load(book.bookImage)
            binding.yearOfBookTv.setText(book.yearOfPublication)
            binding.descerption.setText(book.summary)
          if (auth.currentUser!!.uid == book.bookOwner) {
              binding.editBookBtn.visibility = View.VISIBLE
              Log.d(TAG, "onCreateViewggggg:${auth.currentUser!!.uid},${book.bookOwner} ")
          }

        }.invokeOnCompletion {

            lifecycleScope.launch {
                bookDetailsViewModel.getComment(bookId).observe(
                    viewLifecycleOwner
                ) {
                    binding.commentRv.adapter = CommentAdapter(it)
                }
            }
            Log.d(TAG, "onCreateView: fav ${user.favorite}")
            Log.d(TAG, "onCreateView: condition ${user.favorite.contains(Favorite(book.bookId))}")
            Log.d(TAG, "onCreateView: reverse condition ${!user.favorite.contains(Favorite(book.bookId))}"
            )

            Log.d(TAG, "onCreateView: ${book.comment}")

            book.rating.forEach {
                ratingAverage += it.userRating.toFloat()
            }

            ratingAverage /= book.rating.size

            binding.rateTv.text = ratingAverage.toString()
        }

        binding.sendCommentBtn.setOnClickListener {
            user = User()
            val commentText = binding.commentTv.text.toString()
            val comment = Comment(
                commentText = commentText, useraId = auth.currentUser!!.uid, username = user
                    .username
            )

            bookDetailsViewModel.addComment(comment, bookId)

            lifecycleScope.launch {
                bookDetailsViewModel.getComment(bookId).observe(
                    viewLifecycleOwner
                ) {
                    binding.commentRv.adapter = CommentAdapter(it)
                }

            }
            binding.commentTv.text.clear()
        }

        binding.addToFav.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                lifecycleScope.launch {
                    val favorite = Favorite(bookId)
                    bookDetailsViewModel.addToFavv(favorite, bookId)
                }
            } else {
                lifecycleScope.launch {
                    bookDetailsViewModel.deleteFavorite(bookId)
                }
            }
        }
        binding.commentRv.layoutManager = LinearLayoutManager(context)
        book = Book()
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            val ratingBook =
                RatingBook(userRating = rating.toString(), userId = auth.currentUser!!.uid)
            bookDetailsViewModel.addBookRating(bookId, ratingBook, userId = auth.currentUser!!.uid)

        }

        Log.d(TAG, "onCreateViewggggghhhhh:${auth.currentUser!!.uid},${book.bookOwner} ")



        binding.editBookBtn.setOnClickListener {
            val action =
                BookDetailsFragmentDirections.actionBookDetailsFragmentToEditBookFragment(book.bookId)
            findNavController().navigate(action)
        }

        binding.pdfView.setOnClickListener {
            val action =
                BookDetailsFragmentDirections.actionBookDetailsFragmentToPdfViewFragment(book.bookId)
            findNavController().navigate(action)
        }

        favCheck()
        return binding.root
    }


    private inner class CommentHolder(val binding: CommentListItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private var comment: Comment? = null

        init {
            itemView.setOnClickListener(this)

        }

        fun bind(comment: UserComment) {
            this.comment = comment.comment
            binding.commentTv.text = comment.comment?.commentText
            binding.imageUserTv.load(comment.user?.profileImageUrl)
            binding.usernameTvComment.text = comment.user?.username
            Log.d(TAG, "bind: ${user.username}")
        }

        override fun onClick(v: View?) {

            if (v == itemView) {
                val action =
                    BookDetailsFragmentDirections.actionBookDetailsFragmentToProfileFragment(user.userId)
                findNavController().navigate(action)
            }
        }
    }


    private inner class CommentAdapter(val comments: List<UserComment>) :
        RecyclerView.Adapter<CommentHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
            val binding = CommentListItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )

            return CommentHolder(binding)
        }

        override fun onBindViewHolder(holder: CommentHolder, position: Int) {

            val comment = comments[position]
            holder.bind(comment)
        }

        override fun getItemCount(): Int {
            return comments.size
        }
    }


    private fun favCheck() {
        lifecycleScope.launch {
            bookDetailsViewModel.getUserData().observe(viewLifecycleOwner) { currentUser ->
                Log.d(TAG, "favCheck: ${currentUser.favorite}")
                binding.addToFav.isChecked = currentUser.favorite.contains(Favorite(bookId))
//                binding.usernameTv.text = currentUser.username
            }
        }
    }
}

