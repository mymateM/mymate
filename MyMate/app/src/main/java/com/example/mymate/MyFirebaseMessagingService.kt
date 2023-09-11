package com.example.mymate

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = "FirebaseService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM device token", token)

        
    }
}