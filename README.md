# SMS Relay for Android

A transparent Android app for relaying **new incoming SMS on your own phone** to either a Telegram bot or an HTTPS webhook. It never deletes, blocks, or conceals SMS, and forwarding is off until you explicitly enable it.

## Build and install

Open this folder in Android Studio (JDK 17), wait for Gradle sync, then use **Run** on your connected backup Android phone. On first launch, approve SMS access. Android will show an OS permission prompt.

## Telegram setup

1. Open [@BotFather](https://t.me/BotFather) in Telegram, tap **Start**, then send `/newbot`.
2. Follow the prompts to choose the bot's display name and a unique username ending in `bot`.
3. BotFather will provide a token, such as `123456789:AAExampleSecretToken`. Copy it to SMS Relay's **Bot token** field. Treat this token like a password; anyone holding it can control your bot.
4. Open your new bot in Telegram and tap **Start** or send it a message. Telegram bots cannot begin a private chat with you.
5. In a browser, replace `YOUR_TOKEN` in the following address with your bot token and open it:

   ```text
   https://api.telegram.org/botYOUR_TOKEN/getUpdates
   ```

6. In the returned JSON, find the value like `"chat":{"id":123456789,...}`. Copy the number after `"id"` to SMS Relay's **Chat ID** field.
7. Enable forwarding and tap **Save settings**, then send an SMS to this phone number to test.

Never share a bot token in a chat, screenshot, or public repository. Telegram's [official bot tutorial](https://core.telegram.org/bots/tutorial) documents the BotFather flow and Bot API.

For another service, use an HTTPS endpoint. The app POSTs JSON containing `sender`, `body`, and Unix-milliseconds `receivedAt`; it can include an optional Bearer token.

Keep the backup phone locked and secured. Anyone with its unlock access can change the forwarding destination.
