package com.example.smsrelay

import android.content.Context

enum class Destination { TELEGRAM, WEBHOOK }

data class RelaySettings(
    val enabled: Boolean = false,
    val destination: Destination = Destination.TELEGRAM,
    val telegramToken: String = "",
    val telegramChatId: String = "",
    val webhookUrl: String = "",
    val webhookBearerToken: String = ""
)

class SettingsStore(context: Context) {
    private val prefs = context.getSharedPreferences("relay_settings", Context.MODE_PRIVATE)

    fun read() = RelaySettings(
        enabled = prefs.getBoolean("enabled", false),
        destination = Destination.valueOf(prefs.getString("destination", Destination.TELEGRAM.name)!!),
        telegramToken = prefs.getString("telegram_token", "")!!,
        telegramChatId = prefs.getString("telegram_chat_id", "")!!,
        webhookUrl = prefs.getString("webhook_url", "")!!,
        webhookBearerToken = prefs.getString("webhook_bearer", "")!!
    )

    fun save(value: RelaySettings) = prefs.edit()
        .putBoolean("enabled", value.enabled)
        .putString("destination", value.destination.name)
        .putString("telegram_token", value.telegramToken.trim())
        .putString("telegram_chat_id", value.telegramChatId.trim())
        .putString("webhook_url", value.webhookUrl.trim())
        .putString("webhook_bearer", value.webhookBearerToken.trim())
        .apply()
}
