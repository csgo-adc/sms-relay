package com.example.smsrelay

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object RelayNotification {
    private const val channelId = "relay_status"
    private const val notificationId = 42

    fun update(context: Context, enabled: Boolean) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(NotificationChannel(channelId, "SMS relay status", NotificationManager.IMPORTANCE_LOW))
        if (!enabled) { manager.cancel(notificationId); return }
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle("SMS Relay is enabled")
            .setContentText("New SMS on this phone may be forwarded to your configured destination.")
            .setOngoing(true)
            .build()
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
}
