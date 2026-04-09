package app.toque

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

object RelayNotifier {

    private const val CHANNEL_ID = "toque_calls"

    fun ensureChannel(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        if (manager.getNotificationChannel(CHANNEL_ID) != null) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Llamadas reenviadas",
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = "Notificaciones de llamadas VoIP detectadas por Toque"
            enableVibration(true)
        }
        manager.createNotificationChannel(channel)
    }

    fun postCall(
        context: Context,
        id: Int,
        sourceName: String,
        title: String,
        text: String,
        actions: Array<android.app.Notification.Action>? = null,
    ) {
        ensureChannel(context)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_call)
            .setContentTitle("\uD83D\uDCDE $title")
            .setContentText("$sourceName · $text")
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setAutoCancel(false)
            .setWhen(System.currentTimeMillis())



        actions?.forEach { action ->
            @Suppress("DEPRECATION")
            builder.addAction(action.icon, action.title, action.actionIntent)
        }

        attachDeclineOnDismiss(context, id, actions, builder)

        val notification = builder.build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(id, notification)
    }

    private fun attachDeclineOnDismiss(
        context: Context,
        id: Int,
        actions: Array<android.app.Notification.Action>?,
        builder: NotificationCompat.Builder,
    ) {
        if (actions == null) return
        val declineAction = actions.firstOrNull { action ->
            val label = action.title?.toString()?.lowercase() ?: ""
            DECLINE_LABELS.any { label.contains(it) }
        } ?: return

        @Suppress("DEPRECATION")
        val declinePi = declineAction.actionIntent ?: return
        CallActionStore.register(id, declinePi)

        val dismissIntent = Intent(context, CallDismissReceiver::class.java).apply {
            putExtra(CallDismissReceiver.EXTRA_NOTIF_ID, id)
        }
        val dismissPi = PendingIntent.getBroadcast(
            context, id, dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        builder.setDeleteIntent(dismissPi)
    }

    private val DECLINE_LABELS = listOf(
        "decline", "reject", "rechazar", "colgar", "dismiss", "deny",
    )

    fun cancel(context: Context, id: Int) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.cancel(id)
    }
}
