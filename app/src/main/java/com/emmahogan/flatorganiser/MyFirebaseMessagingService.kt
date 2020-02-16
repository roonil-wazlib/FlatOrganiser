package com.emmahogan.flatorganiser

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage



class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("TEGG", "From: " + p0.from!!)
        Log.d("TEGG", "Notification Message Body: " + p0.notification!!.body!!)
    }
}