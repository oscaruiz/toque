package app.toque

import android.app.Notification

data class IncomingNotification(
    val packageName: String,
    val channelId: String?,
    val category: String?,
    val title: String?,
    val text: String?,
    val isOngoing: Boolean,
    val actions: Array<Notification.Action>? = null,
)

sealed class RelayDecision {
    data class Relay(
        val sourceId: String,
        val sourceName: String,
        val title: String,
        val text: String,
        val actions: Array<Notification.Action>? = null,
    ) : RelayDecision()

    object Ignore : RelayDecision()
}

object CallFilter {

    fun decide(n: IncomingNotification): RelayDecision {
        val source = CallSourceRegistry.findFor(n.packageName)
            ?: return RelayDecision.Ignore

        if (!source.enabled) return RelayDecision.Ignore

        val isCall = matchesChannel(n.channelId, source.channelHints)
            || n.category == Notification.CATEGORY_CALL

        if (!isCall) return RelayDecision.Ignore

        val title = n.title ?: source.displayName
        val text = n.text ?: ""

        return RelayDecision.Relay(
            sourceId = source.id,
            sourceName = source.displayName,
            title = title,
            text = text,
            actions = n.actions,
        )
    }

    private fun matchesChannel(channelId: String?, hints: List<String>): Boolean {
        if (channelId == null) return false
        val lower = channelId.lowercase()
        return hints.any { hint -> lower.contains(hint) }
    }
}
