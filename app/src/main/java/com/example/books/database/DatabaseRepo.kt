package com.example.books.database

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.example.books.Book
import com.example.books.commentFragment.Following
import com.example.books.commentFragment.Validation
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

class DatabaseRepo private constructor(context: Context) {


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storge = FirebaseStorage.getInstance()
    private val storageRef = storge.reference

    private val userCollectionRef = Firebase.firestore.collection("users")
    private val booksCollectionRef = Firebase.firestore.collection("books")


    suspend fun loginUser(email: String, password: String): Boolean {


        var isSuccess: Boolean? = null

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    isSuccess = true
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)


                }


            }.addOnFailureListener {

                isSuccess = false

            }.await()
        return isSuccess!!

    }


    fun getCurrentUserId(): String? {

        return auth.currentUser?.uid

    }


    fun signOut() = auth.signOut()

    suspend fun getBookFromUser(userId: String): LiveData<List<Book>> {

        return liveData {

            booksCollectionRef.whereEqualTo("bookOwner", userId).get().await()
        }
    }

    suspend fun getAudioBookFromUser(userId: String): LiveData<List<AudioBook>> {

        return liveData {

            booksCollectionRef.whereEqualTo("bookOwner", userId).get().await()
        }
    }


    suspend fun uploadImage(curFile: Uri): Boolean {

        var isSuccess = false
        val ref = storageRef.child("image/img_${Calendar.getInstance().time}.jpg")
        val task = ref.putFile(curFile)

        val uriTask = task.continueWithTask { task ->

            if (!task.isSuccessful) {
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
                if (Firebase.auth.currentUser != null) {

                    firestore.collection("users").document(auth.currentUser?.uid!!).update(
                        "profileImageUrl",
                        imageUrl
                    )
                }

//                      .update("profileImageUrl",imageUrl)

                isSuccess = true
            }.addOnFailureListener {
                Log.d(TAG, "error url ")
            }.await()

        return isSuccess


    }

    suspend fun getUserData(): LiveData<User> {

        return liveData {

            val user = userCollectionRef.document(auth.currentUser!!.uid).get().await()
                .toObject(User::class.java)

            if (user != null) {
                emit(user)
            }
        }

    }


    suspend fun deleteFavorite(bookId: String) {

//        to delete a FAVEROTE
        val user = userCollectionRef.document(auth.currentUser!!.uid).get().await()
            .toObject(User::class.java)
        val newFav = user?.favorite!!.filter {
            it.bookId != bookId

        }
        Log.d(TAG, "deleteFavorite: $newFav")
        userCollectionRef.document(auth.currentUser!!.uid).update("favorite", newFav)

    }


    fun saveUser(user: User) {


        val Id = userCollectionRef.document(auth.currentUser!!.uid)
        user.userId = Id.id
        Id.set(user)


    }

    fun savaProfileUserData(user: User) {


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

    suspend fun addToFavv(favorite: Favorite, bookId: String ) {
        userCollectionRef.document(auth.currentUser!!.uid)
            .update("favorite", FieldValue.arrayUnion(favorite))

    }

    suspend fun addAudioBookToFavorite(favorite: Favorite, audioBookId: String ) {
        userCollectionRef.document(auth.currentUser!!.uid)
            .update("favorite", FieldValue.arrayUnion(favorite))

    }

    suspend fun followers(followers: String, userId: String) {

        userCollectionRef.document(auth.currentUser!!.uid)
            .update("followers", FieldValue.arrayUnion(followers))

    }

    suspend fun getUser(userId: List<String>): LiveData<List<User>> {
        val users = mutableListOf<User>()
        return liveData {

            userId.forEach {

                booksCollectionRef.document(it).get().await().toObject(User::class.java)
                    ?.let {
                        users.add(
                            it
                        )
                    }
            }

            emit(users)

        }


    }


    companion object {

        private var INSTANT: DatabaseRepo? = null

        fun initiliza(context: Context) {

            if (INSTANT == null) {
                INSTANT = DatabaseRepo(context)

            }

        }

        fun getInstant(): DatabaseRepo =
            INSTANT ?: throw IllegalStateException(" repo has not be init")

    }
}



