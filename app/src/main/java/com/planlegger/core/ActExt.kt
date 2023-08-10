package com.planlegger.core

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

fun Activity.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.isInternetAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}