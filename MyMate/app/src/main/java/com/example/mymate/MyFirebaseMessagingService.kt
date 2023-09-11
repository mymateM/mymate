package com.example.mymate

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = "FirebaseService"
    lateinit var userRepo: DataStoreRepoUser

    override fun onNewToken(token: String) {
        Log.d("FCM device token", token)
        userRepo = DataStoreRepoUser(dataStore)

        CoroutineScope(IO).launch {
            userRepo.keyDevice(token)
            Log.i("fcmdevicetoken", token)
        }

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("onMsgReceived", "From: " + message!!.from)
        Log.d("onMsgReceived", "Msg Data: ${message.data}")
        Log.d("onMsgReceived", "Msg Noti: ${message.notification}")

        if(message.data.isNotEmpty()) {

        }
    }

    private fun sendNotification(message: RemoteMessage) {
        //notice id(고유값, 개별표시)
        val unlid: Int = (System.currentTimeMillis()/7).toInt()
        //pending intent(1회용)
        val intent = Intent(this, MainActivity::class.java)
        for(key in message.data.keys) {
            intent.putExtra(key, message.data.getValue(key))
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) //Activity Stack clear

        //pendingIntent 더 알아볼 것!
        val pendingIntent = PendingIntent.getActivity(this, unlid, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE)

        val channelId = "my_channel"
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(message.data["title"].toString())
            .setContentText(message.data["body"].toString())
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //오레오 버전 이후에는 채널이 필요 부분부터~
    }
}