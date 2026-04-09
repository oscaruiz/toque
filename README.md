# Toque

Android app that re-posts VoIP call notifications (WhatsApp for now) as its own notifications, so smartwatches whose companion apps (Mi Fitness, Zepp Life, etc.) only filter by package can show calls without the chat spam.

## Status

Alpha — work in progress. WhatsApp only, notification only (no answering from the watch).

## Usage

1. Install Toque on your phone.
2. Open the app and grant notification access permission.
3. In your watch's companion app (Mi Fitness, Zepp Life, Huawei Health, etc.):
   - **Disable** WhatsApp notifications.
   - **Enable** Toque notifications.
4. Done: WhatsApp VoIP calls will buzz your watch, but chats won't.

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
