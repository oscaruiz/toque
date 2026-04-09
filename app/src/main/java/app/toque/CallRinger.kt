package app.toque

import android.app.Notification
import android.content.Context
import android.os.Handler
import android.os.Looper

class CallRinger(
    private val context: Context,
    private val sourceName: String,
    private val title: String,
    private val text: String,
    private val actions: Array<Notification.Action>?,
) {
    private val handler = Handler(Looper.getMainLooper())
    private var currentNotifId: Int = -1
    private var tickCount = 0

    private val ringRunnable = object : Runnable {
        override fun run() {
            if (tickCount * RING_INTERVAL_MS >= MAX_RING_DURATION_MS) {
                stop()
                return
            }
            RelayNotifier.cancel(context, currentNotifId)
            currentNotifId = IdGen.next()
            RelayNotifier.postCall(
                context, currentNotifId, sourceName, title, text, actions,
            )
            tickCount++
            handler.postDelayed(this, RING_INTERVAL_MS)
        }
    }

    fun start(initialId: Int) {
        currentNotifId = initialId
        tickCount = 0
        handler.postDelayed(ringRunnable, RING_INTERVAL_MS)
    }

    fun stop() {
        handler.removeCallbacks(ringRunnable)
        RelayNotifier.cancel(context, currentNotifId)
    }

    companion object {
        const val RING_INTERVAL_MS = 3_000L
        const val MAX_RING_DURATION_MS = 60_000L
    }
}
