package com.example.smsrelay

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (SettingsStore(context).read().enabled) RelayService.sync(context, true)
    }
}
