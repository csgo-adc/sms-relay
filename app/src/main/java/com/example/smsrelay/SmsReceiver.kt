package com.example.smsrelay

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import java.util.concurrent.Executors

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        val pendingResult = goAsync()
        Executors.newSingleThreadExecutor().execute {
            try {
                val settings = SettingsStore(context).read()
                if (!settings.enabled) return@execute
                Telephony.Sms.Intents.getMessagesFromIntent(intent)
                    .groupBy { it.originatingAddress.orEmpty() }
                    .forEach { (sender, parts) ->
                        RelayClient.send(settings, sender, parts.joinToString("") { it.messageBody.orEmpty() }, System.currentTimeMillis())
                    }
            } catch (_: Exception) {
                // SMS remains in the normal phone inbox. Avoid retries that could duplicate a relay.
            } finally { pendingResult.finish() }
        }
    }
}
