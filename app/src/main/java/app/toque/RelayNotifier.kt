package app.toque

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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

        actions?.forEach { action ->
            @Suppress("DEPRECATION")
            builder.addAction(action.icon, action.title, action.actionIntent)
        }

        val notification = builder.build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(id, notification)
    }

    fun cancel(context: Context, id: Int) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.cancel(id)
    }
}
