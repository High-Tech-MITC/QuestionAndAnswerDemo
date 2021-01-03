package com.example.questionandanswerdemo.HelperClass

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


/*
-- checking the internet connection
 */
class ConnectionCheck(var context: Context) {



    public fun networkconnectivity():Boolean {
        val connectivitymanager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network=connectivitymanager.activeNetwork ?:return false
            val activenetwork=connectivitymanager.getNetworkCapabilities(network) ?:return true
            return when {
                activenetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                activenetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                else->false
            }
        }else{
            return connectivitymanager.activeNetworkInfo?.isConnected ?: false
        }
    }


}