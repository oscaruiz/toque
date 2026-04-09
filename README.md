# Toque

Android app that re-posts VoIP call notifications (WhatsApp for now) as its own notifications, so smartwatches whose companion apps (Mi Fitness, Zepp Life, etc.) only filter by package can show calls without the chat spam.

## Status

Alpha — work in progress. WhatsApp only, notification only (no answering from the watch).

## Usage

### 1. Install and configure Toque

1. Install the app on your phone.
2. Open Toque — you'll see a status screen showing whether notification access is granted.
3. Tap **"Conceder acceso"** (Grant access) — this opens the Android system settings for Notification Listener services.
4. In the system screen, find **Toque** in the list and toggle it **on**. Confirm the system warning dialog.
5. Go back to Toque — the status should now show a green **granted** message.

### 2. (Optional) Adjust notification settings

Tap **"Ajustes de notificación de Toque"** (Toque notification settings) to open Android's notification settings for the app. Here you can control the notification channel's sound, vibration, and priority — useful to make sure relayed calls feel distinct on your phone.

### 3. Configure your watch's companion app

In your watch's companion app (Mi Fitness, Zepp Life, Huawei Health, etc.):
- **Disable** WhatsApp notifications.
- **Enable** Toque notifications.

### 4. Done

WhatsApp VoIP calls will buzz your watch, but chats won't. The service survives reboots automatically — no need to reopen the app after restarting your phone.

### Android permissions

| Permission | Why |
|---|---|
| **Notification Listener** (granted in system settings) | Required to read incoming WhatsApp notifications and detect VoIP calls. |
| **Post Notifications** (Android 13+) | Required to post the relayed call notification that your watch will pick up. |
| **Receive Boot Completed** | Ensures the service reconnects automatically after a phone reboot. |

## Build

```
./gradlew assembleDebug
```

## Install

```
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Capture discovery logs

With the app installed and notification access granted:

```
adb logcat -s Toque:D
```

Trigger WhatsApp events (incoming call, video call, message, group, status) and watch for lines tagged `Toque/Discovery`.

## Comparison with alternatives

| Project | OSS | Zero-config | Specific purpose | Notes |
|---|---|---|---|---|
| **Toque** | Yes (GPL-3.0) | Yes | VoIP → wearable notification-only | Small, no extra dependencies |
| MacroDroid | No | No | General automation | Requires manual macro setup |
| FilterBox | No | No | General notification filtering | Has repost-as-call, but paid |
| Notify for Amazfit (OneZeroBit) | No | Yes | Full companion app | Supports many apps, but closed-source and paid |
| Filter-Notification-For-Smart-Band | Yes | No | Mi Band 4 specific | Abandoned |
| NotiFilter (BURG3R5) | Yes | No | Silence/throttle | Does not relay notifications |
| Gadgetbridge | Yes (AGPL) | No | Generic companion | Channel filtering, but doesn't support all watches (e.g. Xiaomi Mi Watch 2020) |

## License

GPL-3.0 — see [LICENSE](LICENSE).
