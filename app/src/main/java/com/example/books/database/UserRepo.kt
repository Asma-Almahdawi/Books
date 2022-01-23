package com.example.books.database

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.books.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*

private const val TAG = "DatabaseRepo"

class UserRepo private constructor(context: Context) {


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
        }.addOnSuccessListener {
            val imageUrl = it.toString()
            if (Firebase.auth.currentUser != null) {
                firestore.collection("users").document(auth.currentUser?.uid!!).update(
                    "profileImageUrl",
                    imageUrl
                )
            }
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


    suspend fun addToFavv(favorite: Favorite, bookId: String) {
        userCollectionRef.document(auth.currentUser!!.uid)
            .update("favorite", FieldValue.arrayUnion(favorite))
    }

    suspend fun addAudioBookToFavorite(favorite: Favorite, audioBookId: String) {
        userCollectionRef.document(auth.currentUser!!.uid)
            .update("favorite", FieldValue.arrayUnion(favorite))
    }

    suspend fun followers(followers: String, userId: String) {
        userCollectionRef.document(auth.currentUser!!.uid)
            .update("followers", FieldValue.arrayUnion(followers))
    }


    companion object {

        private var INSTANT: UserRepo? = null

        fun initiliza(context: Context) {

            if (INSTANT == null) {
                INSTANT = UserRepo(context)
            }
        }

        fun getInstant(): UserRepo =
            INSTANT ?: throw IllegalStateException(" repo has not be init")
    }
}

