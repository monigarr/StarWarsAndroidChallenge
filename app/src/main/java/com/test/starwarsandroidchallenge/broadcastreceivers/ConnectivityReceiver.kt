package com.test.starwarsandroidchallenge.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.test.starwarsandroidchallenge.util.NetworkHelper

class ConnectivityReceiver(private var connectivityReceiverListener: ConnectivityReceiverListener? = null) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        connectivityReceiverListener?.onNetworkConnectionChanged(NetworkHelper.isNetworkConnectionAvailable(context))
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
    }
}