package com.test.starwarsandroidchallenge.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceManager {
    private constructor()

    companion object {
        private var sharedPreferens: SharedPreferences? = null

        fun init(context: Context) {
            sharedPreferens = context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
        }

        @SuppressLint("ApplySharedPref")
        fun putString(key: String, value: String) {
            sharedPreferens?.edit()?.putString(key, value)?.commit()
        }

        fun getString(key: String) : String? {
            return sharedPreferens?.getString(key, Constants.PEOPLE_LIST_INITIAL_URL)
        }

        /**
         * This method returns constant string for only networking purpose.
         * It is a specific method.
         */
        fun getNetworkCalliingUrl(key: String) : String? {
            return sharedPreferens?.getString(key, Constants.PEOPLE_LIST_INITIAL_URL)
        }

        fun removeString(key: String) {
            sharedPreferens?.edit()?.remove(key)?.commit()
        }
    }
}