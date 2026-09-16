# SMS Relay for Android

A transparent Android app for relaying **new incoming SMS on your own phone** to either a Telegram bot or an HTTPS webhook. It never deletes, blocks, or conceals SMS, and forwarding is off until you explicitly enable it.

## Build and install

Open this folder in Android Studio (JDK 17), wait for Gradle sync, then use **Run** on your connected backup Android phone. On first launch, approve SMS access. Android will show an OS permission prompt.

## Telegram setup

1. Create a bot via Telegram's `@BotFather` and copy its token.
2. Send the bot a message.
3. Find your chat ID (for example, call `getUpdates` using the Telegram Bot API after messaging the bot).
4. Enter both values, enable forwarding, and tap **Save settings**.

For another service, use an HTTPS endpoint. The app POSTs JSON containing `sender`, `body`, and Unix-milliseconds `receivedAt`; it can include an optional Bearer token.

Keep the backup phone locked and secured. Anyone with its unlock access can change the forwarding destination.
