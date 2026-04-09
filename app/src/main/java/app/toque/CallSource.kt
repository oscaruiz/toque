package app.toque

data class CallSource(
    val id: String,
    val displayName: String,
    val packageNames: Set<String>,
    val channelHints: List<String>,
    val enabled: Boolean = true,
)

object CallSourceRegistry {

    private val sources = listOf(
        // TODO: validate channelHints with discovery logs from a real device
        CallSource(
            id = "whatsapp",
            displayName = "WhatsApp",
            packageNames = setOf("com.whatsapp", "com.whatsapp.w4b"),
            channelHints = listOf("voip", "incoming_call", "call"),
        ),

        // PHASE3: Telegram
        // CallSource(
        //     id = "telegram",
        //     displayName = "Telegram",
        //     packageNames = setOf(
        //         "org.telegram.messenger",
        //         "org.telegram.messenger.web",
        //         "org.thunderdog.challegram",
        //     ),
        //     channelHints = listOf(), // unknown — needs discovery
        // ),

        // PHASE3: Signal
        // CallSource(
        //     id = "signal",
        //     displayName = "Signal",
        //     packageNames = setOf("org.thoughtcrime.securesms"),
        //     channelHints = listOf(), // unknown — needs discovery
        // ),

        // PHASE3: Messenger
        // CallSource(
        //     id = "messenger",
        //     displayName = "Messenger",
        //     packageNames = setOf("com.facebook.orca"),
        //     channelHints = listOf(), // unknown — needs discovery
        // ),

        // PHASE3: Google Meet
        // CallSource(
        //     id = "meet",
        //     displayName = "Google Meet",
        //     packageNames = setOf("com.google.android.apps.tachyon"),
        //     channelHints = listOf(), // unknown — needs discovery
        // ),
    )

    fun findFor(packageName: String): CallSource? =
        sources.firstOrNull { source ->
            source.packageNames.any { it.equals(packageName, ignoreCase = true) }
        }
}
