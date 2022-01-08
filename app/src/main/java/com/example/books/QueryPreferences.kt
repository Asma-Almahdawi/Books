package com.example.books

import android.content.Context
import androidx.preference.PreferenceManager
private const val PREF_SEARCH_QUERY_KEY = "searchQuery"
class QueryPreferences {

    fun getStoredQuery(context: Context):String{

        val pref = PreferenceManager.getDefaultSharedPreferences(context)
      return pref.getString("language", "")!!

//        return  pref.getString(PREF_SEARCH_QUERY_KEY,"")!!
    }

    fun setStoredQuery(context: Context, query: String){
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SEARCH_QUERY_KEY,query)
            .apply()
    }



}