package com.example.mymate

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.internal.notify
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = "FirebaseService"
    lateinit var userRepo: DataStoreRepoUser
    val CHANNEL_ID = "myMate"
    val CHANNEL_NAME = "MyMate"

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

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        var builder : NotificationCompat.Builder

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
                }
            builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        } else {
            builder = NotificationCompat.Builder(applicationContext)
        }

        val title = message.notification?.title
        val body = message.notification?.body

        builder.setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_launcher_background)

        val notification = builder.build()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        }
        notificationManager.notify(1, notification)
    }

    /*private fun sendNotification(message: RemoteMessage) {
        //notice id(고유값, 개별표시)
        val unlid: Int = (System.currentTimeMillis()/7).toInt()
        //pending intent(1회용)
        val intent = Intent(this, AlarmActivity::class.java)
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

        //채널 설정

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(unlid, notificationBuilder.build())
    }*/

    fun getFirebaseToken(): String {
        var firebasedevicecode = ""
        userRepo = DataStoreRepoUser(dataStore)
        runBlocking {
            var getToken = launch {
                firebasedevicecode = userRepo.userDeviceReadFlow.first().toString()
            }
            getToken.join()
        }
        Log.d("getfcmdevicetoken", firebasedevicecode)
        return firebasedevicecode
    }

    fun getFirebaseToken2(): String {
        var token = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener{ task ->
            if(!task.isSuccessful) {
                Log.w("TestDeviceToken", "FetchingFCMregistrationtokenfailed", task.exception)
                return@OnCompleteListener
            }
            token = task.result
            Log.d("TestDeviceToken", token)
        })
        return token
    }

    fun sendFirebaseToken() {
        userRepo = DataStoreRepoUser(dataStore)
        var firebasedevicecode = ""
        var retrofit = RetrofitClientInstance.client
        var endpoint = retrofit?.create(localDevice::class.java)
        var deviceToken: devicetoken = devicetoken()
        var accessToken: String = ""
        runBlocking {
            var getAC = launch {
                accessToken = userRepo.getAccessToken()
                Log.d(TAG, "accessToken = ${accessToken}")
            }
            getAC.join()
            var getToken = launch {
                firebasedevicecode = userRepo.userDeviceReadFlow.first().toString()
            }
            getToken.join()
        }
        deviceToken.deviceToken = firebasedevicecode
        endpoint!!.localDevice("Bearer " + accessToken, deviceToken).enqueue(object : Callback<Response<Void>> {
            override fun onResponse(
                call: Call<Response<Void>>,
                response: Response<Response<Void>>
            ) {
                Log.i("localDevice", deviceToken.deviceToken + " success")
            }

            override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                Log.e("localDevice", "failed to connect")
            }
        })
    }
}