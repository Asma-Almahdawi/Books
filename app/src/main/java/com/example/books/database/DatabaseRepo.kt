package com.example.books.database

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

import java.lang.Exception

private const val TAG = "DatabaseRepo"
class DatabaseRepo(){


    private val userCollectionRef = Firebase.firestore.collection("users")

//    var user:List<User> = mutableListOf()
    fun saveUser(user:User) = CoroutineScope(Dispatchers.IO).launch {
//        var usersData:List<User> = emptyList()
        try {


            userCollectionRef.add(user).addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }


            withContext(Dispatchers.Main) {
                Log.d(TAG, "SAVE DATA ")

            }

        } catch (e: Exception) {

            withContext(Dispatchers.Main) {

                Log.d(TAG, " cannot SAVE DATA ")

            }

        }

    }

     }

//    companion object{
//
//        private var INSTANT:DatabaseRepo?=null
//
//        fun initialize(context: Context){
//
//            if (INSTANT==null){
//
//                INSTANT= DatabaseRepo(context)
//            }
//
//        }
//
//        fun get():DatabaseRepo{
//
//            return INSTANT?:
//            throw IllegalStateException("TaskRepository must be initialize")
//        }
//
//    }


