package com.test.starwarsandroidchallenge.util

import android.content.Context
import android.net.ConnectivityManager

class NetworkHelper {
    private constructor()

    companion object {
        fun isNetworkConnectionAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }
}