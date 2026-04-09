package app.toque

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationRelayService : NotificationListenerService() {

    private val activeRingers = mutableMapOf<String, CallRinger>()

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val notification = sbn.notification
        val extras = notification.extras
        val channelId = notification.channelId
        val category = notification.category
        val isOngoing = notification.flags and Notification.FLAG_ONGOING_EVENT != 0
        val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()

        if (BuildConfig.DEBUG && sbn.packageName.contains("whatsapp", ignoreCase = true)) {
            Log.d(
                TAG_DISCOVERY,
                "pkg=${sbn.packageName} ch=$channelId cat=$category ong=$isOngoing " +
                    "title=$title text=$text flags=${notification.flags}"
            )
        }

        val incoming = IncomingNotification(
            packageName = sbn.packageName,
            channelId = channelId,
            category = category,
            title = title,
            text = text,
            isOngoing = isOngoing,
            actions = notification.actions,
        )

        when (val decision = CallFilter.decide(incoming)) {
            is RelayDecision.Relay -> {
                if (sbn.key in activeRingers) return

                val id = IdGen.next()
                RelayNotifier.postCall(
                    context = this,
                    id = id,
                    sourceName = decision.sourceName,
                    title = decision.title,
                    text = decision.text,
                    actions = decision.actions,
                )

                val ringer = CallRinger(
                    context = this,
                    sourceName = decision.sourceName,
                    title = decision.title,
                    text = decision.text,
                    actions = decision.actions,
                )
                ringer.start(id)
                activeRingers[sbn.key] = ringer
            }
            is RelayDecision.Ignore -> { /* nothing */ }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        val ringer = activeRingers.remove(sbn.key) ?: return
        ringer.stop()
    }

    override fun onListenerDisconnected() {
        activeRingers.values.forEach { it.stop() }
        activeRingers.clear()
        super.onListenerDisconnected()
    }

    companion object {
        private const val TAG_DISCOVERY = "Toque/Discovery"
    }
}
