package com.example.books.fragments.AudioBookDetail

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.books.Book
import com.example.books.R
import com.example.books.commentFragment.Comment
import com.example.books.commentFragment.UserComment
import com.example.books.database.AudioBook
import com.example.books.database.Favorite
import com.example.books.database.RatingBook
import com.example.books.database.User
import com.example.books.databinding.CommentListItemBinding
import com.example.books.databinding.FragmentAudioBookDetailsBinding
import com.example.books.fragments.bookdetails.BookDetailsFragmentArgs
import com.example.books.fragments.bookdetails.BookDetailsViewModel
import com.google.firebase.auth.FirebaseAuth

import kotlinx.coroutines.launch
import java.io.IOException

private const val TAG = "AudioBookDetailsFragment"

class AudioBookDetailsFragment : Fragment() {

    private val audioBookDetailsViewModel by lazy { ViewModelProvider(this)[AudioBookDetailsViewModel::class.java] }

    private lateinit var binding: FragmentAudioBookDetailsBinding
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioBook: AudioBook
    private val args: AudioBookDetailsFragmentArgs by navArgs()
    lateinit var audioBookId: String
    private lateinit var user: User
    private lateinit var auth: FirebaseAuth
    var ratingAverage = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        audioBook = AudioBook()
        mediaPlayer = MediaPlayer()
        auth = FirebaseAuth.getInstance()
        audioBookId = args.audioBookId
        Log.d(TAG, "onCreate: $audioBookId")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentAudioBookDetailsBinding.inflate(layoutInflater)

        lifecycleScope.launch {

            audioBook = audioBookDetailsViewModel.getAudioBook(audioBookId) ?: AudioBook()
            binding.BookNameTv.setText(audioBook.bookName)
            binding.authorNameTv.setText(audioBook.bookOwner)
            binding.authorNameTv.setText(audioBook.authorName)
            binding.imageBookTv.load(audioBook.bookImage)
            binding.yearOfBookTv.setText(audioBook.yearOfPublication)
            binding.descerption.setText(audioBook.summary)

        }.invokeOnCompletion {

            lifecycleScope.launch {
                audioBookDetailsViewModel.getAudioBookComment(audioBookId).observe(
                    viewLifecycleOwner
                ) {

                    binding.commentRv.adapter = CommentAdapter(it)
                }
            }

            audioBook.rating.forEach {
                ratingAverage += it.userRating.toFloat()

            }

            ratingAverage /= audioBook.rating.size
            binding.rateTv.text = ratingAverage.toString()
        }

        binding.seekbar.max = mediaPlayer.duration
        binding.seekbar.progress = 0
        binding.playBtn.setOnClickListener {

            if (!mediaPlayer.isPlaying) {
                playAudio()
                initSeekbar()
                binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_24)
            } else {
                pauseAudio()
                binding.playBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }


            binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
                    if (p2) {
                        mediaPlayer.seekTo(currentValue)
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
        }

        binding.sendCommentBtn.setOnClickListener {

            user = User()
            val commentText = binding.commentTv.text.toString()
            val comment = Comment(
                commentText = commentText, useraId = auth.currentUser!!.uid, username = user
                    .username
            )
            audioBookDetailsViewModel.addAudioBookComment(comment, audioBookId)
        }

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->

            val ratingBook =
                RatingBook(userRating = rating.toString(), userId = auth.currentUser!!.uid)
            audioBookDetailsViewModel.audioBookRating(
                audioBookId,
                ratingBook,
                userId = auth.currentUser!!.uid
            )
        }

        return binding.root
    }

    private fun playAudio() {

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(audioBook.audioFile)

            mediaPlayer.prepare()
            mediaPlayer.start()

        } catch (e: IOException) {

        }
    }

    private fun pauseAudio() {

        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            mediaPlayer.stop()

        } else {

        }
    }

    private fun initSeekbar() {

        binding.seekbar.max = mediaPlayer.duration
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekbar.progress = mediaPlayer.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    binding.seekbar.progress = 0
                }
            }
        }, 0)
    }

    private inner class CommentHolder(val binding: CommentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var comment: Comment? = null

        fun bind(comment: UserComment) {

            this.comment = comment.comment
            binding.commentTv.text = comment.comment?.commentText
            binding.imageUserTv.load(comment.user?.profileImageUrl)
            binding.usernameTvComment.text = comment.user?.username
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
}