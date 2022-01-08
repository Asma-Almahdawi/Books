package com.example.books.database

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.books.commentFragment.Following
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*

private const val TAG = "DatabaseRepo"
class DatabaseRepo private constructor(context: Context){




    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore:FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storge = FirebaseStorage.getInstance()
    private val storageRef = storge.reference

    private val userCollectionRef = Firebase.firestore.collection("users")
    val userCollectionRef1 = Firebase.firestore.collection("users").document(auth.currentUser!!.uid)
    private val booksCollectionRef = Firebase.firestore.collection("books")
    fun flowers(flowers:String){
        userCollectionRef1.update("flowers", FieldValue.arrayUnion(flowers))
        userCollectionRef1.let {

        }

    }


    suspend fun uploadImage(curFile: Uri): Boolean{

        var isSuccess = false
            val ref =  storageRef.child("image/img_${Calendar.getInstance().time}.jpg")
            val task =ref.putFile(curFile)

            val uriTask = task.continueWithTask{task ->

                if (!task.isSuccessful){
                    task.exception?.let {
                        throw it
                    }

                }

                ref.downloadUrl
            }
                .addOnSuccessListener {

                    val imageUrl = it.toString()
//                  Firebase.firestore.collection("users").document(Firebase.auth.currentUser?.uid!!).set(
//                      hashMapOf("imageUrl" to imageUrl))
                    if(Firebase.auth.currentUser != null){

                        firestore.collection("users").document(auth.currentUser?.uid!!).update("profileImageUrl",
                            imageUrl)
                    }

//                      .update("profileImageUrl",imageUrl)

                    isSuccess = true
                }.addOnFailureListener {
                    Log.d(TAG,"error url ")
                }.await()

        return isSuccess


        }
    suspend fun getUserData(): LiveData<User> {

        return liveData {

         val user =   userCollectionRef1.get().await().toObject(User::class.java)

            //to delete a FAVEROTE
//            val newFav = user?.favorite!!.filter {
//                it.bookId != id
//            }
//            userCollectionRef1.set(newFav)

            if (user != null) {
                emit(user)
            }
        }

    }

    suspend fun deleteFav(bookId:String){

//        to delete a FAVEROTE
        val user =   userCollectionRef1.get().await().toObject(User::class.java)
            val newFav = user?.favorite!!.filter {
                it.bookId != bookId
            }
            userCollectionRef1.set(newFav)

    }





    fun saveUser(user: User){


        val Id =  userCollectionRef.document(auth.currentUser!!.uid)
       user.userId = Id.id
        Id.set(user)


    }

    fun savaProfileUserData(user: User){


        val uidRef = userCollectionRef.document(user.userId)
        uidRef.get().addOnSuccessListener { doc ->
            if (doc != null) {
                val user = doc.toObject(User::class.java)
                Log.d(TAG, "{$user.firstName} {$user.lastName}")
            } else {
                Log.d(TAG, "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }


    }

   suspend fun addToFavv(favorite: Favorite , bookId: String){
       userCollectionRef1.update("favorite" , FieldValue.arrayUnion(favorite))


    }



    companion object{

        private var INSTANT : DatabaseRepo? = null

        fun initiliza(context: Context){

            if (INSTANT == null){
                INSTANT = DatabaseRepo(context)

            }

        }

        fun getInstant():DatabaseRepo = INSTANT ?: throw IllegalStateException(" repo has not be init")

    }
}



