package com.example.smsrelay

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    private val permissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNeededPermissions()
        val store = SettingsStore(this)
        RelayNotification.update(this, store.read().enabled)
        RelayService.sync(this, store.read().enabled)
        setContent { RelayScreen(store) {
            RelayNotification.update(this, it)
            RelayService.sync(this, it)
        } }
    }

    private fun requestNeededPermissions() {
        val needed = buildList {
            if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) add(Manifest.permission.RECEIVE_SMS)
            if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (needed.isNotEmpty()) permissions.launch(needed.toTypedArray())
    }
}

@Composable
private fun RelayScreen(store: SettingsStore, updateNotification: (Boolean) -> Unit) {
    var settings by remember { mutableStateOf(store.read()) }
    var status by remember { mutableStateOf("") }
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("SMS Relay", style = MaterialTheme.typography.headlineMedium)
                Text("For messages received by this phone. SMS is never deleted or hidden.")
                Row { Switch(settings.enabled, { settings = settings.copy(enabled = it) }); Spacer(Modifier.width(10.dp)); Text(if (settings.enabled) "Forwarding enabled" else "Forwarding disabled") }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(settings.destination == Destination.TELEGRAM, { settings = settings.copy(destination = Destination.TELEGRAM) }, { Text("Telegram bot") })
                    FilterChip(settings.destination == Destination.WEBHOOK, { settings = settings.copy(destination = Destination.WEBHOOK) }, { Text("HTTPS webhook") })
                }
                if (settings.destination == Destination.TELEGRAM) {
                    Field("Bot token", settings.telegramToken, true) { settings = settings.copy(telegramToken = it) }
                    Field("Chat ID", settings.telegramChatId) { settings = settings.copy(telegramChatId = it) }
                    Text("Create a bot with @BotFather, message it once, then use your chat ID.", style = MaterialTheme.typography.bodySmall)
                } else {
                    Field("HTTPS endpoint", settings.webhookUrl) { settings = settings.copy(webhookUrl = it) }
                    Field("Bearer token (optional)", settings.webhookBearerToken, true) { settings = settings.copy(webhookBearerToken = it) }
                    Text("JSON: sender, body, receivedAt. Only HTTPS endpoints are accepted.", style = MaterialTheme.typography.bodySmall)
                }
                Button(onClick = { store.save(settings); updateNotification(settings.enabled); status = "Saved. Forwarding applies to new incoming SMS." }, modifier = Modifier.fillMaxWidth()) { Text("Save settings") }
                if (status.isNotBlank()) Text(status, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun Field(label: String, value: String, secret: Boolean = false, update: (String) -> Unit) {
    OutlinedTextField(value, update, Modifier.fillMaxWidth(), label = { Text(label) }, singleLine = true,
        visualTransformation = if (secret) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None)
}
