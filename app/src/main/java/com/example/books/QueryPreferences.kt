package com.example.books

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val PREF_SEARCH_QUERY_KEY = "searchQuery"
const val PREFERENCE_NAME = "SharedPre"
const val PREFERENCE_LANGUAGE = "language"

class QueryPreferences(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getLoginCount(): String? {

        return preferences.getString(PREFERENCE_LANGUAGE, "en")
    }

    fun setLoginCount(Language: String) {

        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString(PREFERENCE_LANGUAGE, Language)
        editor.apply()
    }
}