package com.example.books

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
private const val PREF_SEARCH_QUERY_KEY = "searchQuery"
val PREFERENCE_NAME ="SharedPre"
val PREFERENCE_LANGUAGE = "language"
class QueryPreferences(context: Context) {

    val preferences:SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME , Context.MODE_PRIVATE)

fun getLoginCount():String? {

    return preferences.getString(PREFERENCE_LANGUAGE,"en")


}

 fun setLoginCount(Language:String){

     val editor:SharedPreferences.Editor = preferences.edit()
     editor.putString(PREFERENCE_LANGUAGE,Language)
     editor.apply()
 }
//    fun getStoredQuery(context: Context):String{
//
//        val pref = PreferenceManager.getDefaultSharedPreferences(context)
//      return pref.getString("language", "")!!
//
////        return  pref.getString(PREF_SEARCH_QUERY_KEY,"")!!
//    }
//
//    fun setStoredQuery(context: Context, query: String){
//        PreferenceManager.getDefaultSharedPreferences(context)
//            .edit()
//            .putString(PREF_SEARCH_QUERY_KEY,query)
//            .apply()
//    }




}