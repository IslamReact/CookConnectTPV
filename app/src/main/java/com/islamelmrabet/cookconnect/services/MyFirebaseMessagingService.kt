package com.islamelmrabet.cookconnect.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.islamelmrabet.cookconnect.MainActivity
import com.islamelmrabet.cookconnect.R
import kotlin.random.Random

class MyFirebaseService: FirebaseMessagingService() {
    private val random = Random

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            sendNotification(it.body)
        }
    }

    private fun sendNotification(messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, "default")
            .setSmallIcon(R.drawable._cropped)
            .setContentTitle("PEDIDO!")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
    override fun onNewToken(token: String) {
        Log.d("FCM","New token: $token")
    }

    companion object {
        const val CHANNEL_NAME = "FCM notification channel"
    }
}