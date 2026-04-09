package app.toque

import android.app.PendingIntent

object CallActionStore {

    private val declineIntents = mutableMapOf<Int, PendingIntent>()

    fun register(notifId: Int, declineIntent: PendingIntent) {
        declineIntents[notifId] = declineIntent
    }

    fun consume(notifId: Int): PendingIntent? = declineIntents.remove(notifId)

    fun remove(notifId: Int) {
        declineIntents.remove(notifId)
    }
}
