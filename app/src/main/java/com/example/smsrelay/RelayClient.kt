package com.example.smsrelay

import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object RelayClient {
    fun send(settings: RelaySettings, sender: String, body: String, receivedAt: Long) {
        val text = "New SMS\nFrom: $sender\n\n$body"
        when (settings.destination) {
            Destination.TELEGRAM -> telegram(settings, text)
            Destination.WEBHOOK -> webhook(settings, sender, body, receivedAt)
        }
    }

    private fun telegram(settings: RelaySettings, text: String) {
        require(settings.telegramToken.isNotBlank() && settings.telegramChatId.isNotBlank()) { "Telegram details are missing" }
        postForm("https://api.telegram.org/bot${settings.telegramToken}/sendMessage", mapOf("chat_id" to settings.telegramChatId, "text" to text))
    }

    private fun webhook(settings: RelaySettings, sender: String, body: String, receivedAt: Long) {
        require(settings.webhookUrl.startsWith("https://")) { "Webhook must use HTTPS" }
        val payload = JSONObject().put("sender", sender).put("body", body).put("receivedAt", receivedAt).toString()
        val connection = (URL(settings.webhookUrl).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"; connectTimeout = 15_000; readTimeout = 15_000; doOutput = true
            setRequestProperty("Content-Type", "application/json")
            if (settings.webhookBearerToken.isNotBlank()) setRequestProperty("Authorization", "Bearer ${settings.webhookBearerToken}")
        }
        connection.outputStream.use { it.write(payload.toByteArray()) }
        check(connection.responseCode in 200..299) { "Webhook returned ${connection.responseCode}" }
        connection.disconnect()
    }

    private fun postForm(endpoint: String, fields: Map<String, String>) {
        val data = fields.entries.joinToString("&") { "${it.key}=${java.net.URLEncoder.encode(it.value, "UTF-8")}" }
        val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"; connectTimeout = 15_000; readTimeout = 15_000; doOutput = true
            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        }
        OutputStreamWriter(connection.outputStream).use { it.write(data) }
        check(connection.responseCode in 200..299) { "Telegram returned ${connection.responseCode}" }
        connection.disconnect()
    }
}
