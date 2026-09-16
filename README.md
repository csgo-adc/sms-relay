# SMS Relay for Android

SMS Relay forwards new incoming messages from your Android phone to your Telegram bot, group, channel, or HTTPS webhook. It keeps your messages in the normal inbox, shows a visible status notification while enabled, and only forwards after you turn it on.

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

### Send relayed SMS to a group or channel

Use the same bot token and get the destination's chat ID from `getUpdates`:

1. **Group:** create or open a Telegram group, add the bot as a member, then send a normal message or a command such as `/start` in the group.
2. **Channel:** create or open a channel, add the bot as an administrator, allow it to post messages, then publish a test post in the channel. A bot needs administrator access to receive channel-post updates.
3. Open `https://api.telegram.org/botYOUR_TOKEN/getUpdates` in a browser. Locate the update containing either `"chat": {"id": ...}` (group) or `"channel_post": {"chat": {"id": ...}}` (channel).
4. Copy that `id` number into **Chat ID** in SMS Relay and save. Group and channel IDs are commonly negative numbers; channel IDs often begin with `-100`. Keep the minus sign.

You can use your private chat, a group, or a channel as the destination. The bot must already have access to the destination chat before forwarding is enabled.

## Background operation

When forwarding is enabled, SMS Relay shows a persistent Android notification and starts a visible foreground relay service. Incoming SMS are also handled by Android's SMS receiver, so closing the app window does not disable forwarding. The service resumes after a normal device restart while forwarding remains enabled.

Android prevents any app from restarting after a **Force stop**, and battery restriction modes can prevent background work. In your phone's app battery settings, choose **Unrestricted** or **No restrictions** for SMS Relay if that option is available. The persistent “SMS Relay is enabled” notification confirms that the service is active.

For another service, use an HTTPS endpoint. The app POSTs JSON containing `sender`, `body`, and Unix-milliseconds `receivedAt`; it can include an optional Bearer token.

Keep the backup phone locked and secured. Anyone with its unlock access can change the forwarding destination.
