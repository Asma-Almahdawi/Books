package com.example.books.fragments.likespagefragment

import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.books.Book
import com.example.books.R
import com.example.books.commentFragment.Comment
import com.example.books.database.Favorite
import com.example.books.databinding.CommentListItemBinding
import com.example.books.databinding.FavoriteListItemBinding
import com.example.books.databinding.LikePageFragmentBinding
import com.example.books.fragments.homepagefragment.HomePageFragmentDirections
import kotlinx.coroutines.launch

private const val TAG = "LikePageFragment"
class LikePageFragment : Fragment() {

    private lateinit var binding: LikePageFragmentBinding
//    lateinit var bookId:String

    private val  viewModel by lazy { ViewModelProvider(this) [LikePageViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
     binding= LikePageFragmentBinding.inflate(layoutInflater)

        binding.favRv.layoutManager=LinearLayoutManager(context)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.getUserData().observe(
                viewLifecycleOwner, Observer {
                    lifecycleScope.launch {
                        viewModel.getFav(it.favorite).observe(viewLifecycleOwner, { books ->
                            Log.d(TAG, "onViewCreated: $books")
                            binding.favRv.adapter=FavoriteAdapter(books)

                        })
                    }


                }

            )
        }




    }


    private inner class FavoriteHolder(val binding: FavoriteListItemBinding): RecyclerView.ViewHolder(binding.root) ,View.OnClickListener {
        private lateinit var book: Book

        init {
            itemView.setOnClickListener(this)
            binding.deleteFavBtn.setOnClickListener(this)
        }
        fun bind(book: Book){

            this.book = book
            binding.BookFavNameTv.text =book.bookName
            binding.bookFavTv.load(book.bookImage)



            }
        override fun onClick(v: View?) {
            when(v){
                binding.deleteFavBtn -> {
                  lifecycleScope.launch {

                      viewModel.deleteFav(book.bookId)

                  }
                }
                itemView->{
                    val action = LikePageFragmentDirections.actionLikePageFragmentToBookDetailsFragment(book.bookId)
                    Log.d(TAG, "onClick: ${book.bookId}")
                    findNavController().navigate(action)
                }
            }

        }



    }






    private inner class FavoriteAdapter(val books :List<Book>): RecyclerView.Adapter<FavoriteHolder> (){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
            val binding = FavoriteListItemBinding.inflate(
                layoutInflater,
                parent,
                false
            )

            return FavoriteHolder(binding)
        }

        override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {

            val book = books[position]
            holder.bind(book)


        }

        override fun getItemCount(): Int {

            return books.size
            Log.d(TAG, "getItemCount:${books.size} ")
        }


    }





}