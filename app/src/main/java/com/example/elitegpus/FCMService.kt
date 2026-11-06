package com.example.elitegpus

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)

        val t = msg.notification?.title ?: "Elite Push"
        val b = msg.notification?.body ?: "Mensaje del administrador"

        val builder = NotificationCompat.Builder(this, "elite_ch")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(t)
            .setContentText(b)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            NotificationManagerCompat.from(this).notify(99, builder.build())
        }
    }
}