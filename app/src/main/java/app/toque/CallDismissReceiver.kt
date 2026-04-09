package app.toque

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class CallDismissReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notifId = intent.getIntExtra(EXTRA_NOTIF_ID, -1)
        if (notifId == -1) return

        val declineIntent = CallActionStore.consume(notifId) ?: return
        try {
            declineIntent.send()
        } catch (e: Exception) {
            Log.w(TAG, "Failed to send decline intent", e)
        }
    }

    companion object {
        const val EXTRA_NOTIF_ID = "notification_id"
        private const val TAG = "Toque/Dismiss"
    }
}
