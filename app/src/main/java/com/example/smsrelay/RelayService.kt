package com.example.smsrelay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat

/** Keeps the enabled relay visible and less susceptible to background process removal. */
class RelayService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(RelayNotification.notificationId, RelayNotification.build(this))
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        fun sync(context: Context, enabled: Boolean) {
            val intent = Intent(context, RelayService::class.java)
            if (enabled) ContextCompat.startForegroundService(context, intent)
            else context.stopService(intent)
        }
    }
}
