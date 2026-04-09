package app.toque

import android.app.Notification
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CallFilterTest {

    private fun notification(
        packageName: String = "com.whatsapp",
        channelId: String? = null,
        category: String? = null,
        title: String? = null,
        text: String? = null,
        isOngoing: Boolean = false,
        actions: Array<Notification.Action>? = null,
    ) = IncomingNotification(packageName, channelId, category, title, text, isOngoing, actions)

    @Test
    fun `ignores unknown package`() {
        val decision = CallFilter.decide(notification(packageName = "com.example.app"))
        assertTrue(decision is RelayDecision.Ignore)
    }

    @Test
    fun `ignores whatsapp non-call channel`() {
        val decision = CallFilter.decide(
            notification(channelId = "individual_chat_defaults_01")
        )
        assertTrue(decision is RelayDecision.Ignore)
    }

    @Test
    fun `relays whatsapp voip channel`() {
        val decision = CallFilter.decide(
            notification(channelId = "voip_channel", title = "Alice", text = "Llamada entrante")
        )
        assertTrue(decision is RelayDecision.Relay)
        val relay = decision as RelayDecision.Relay
        assertEquals("whatsapp", relay.sourceId)
        assertEquals("WhatsApp", relay.sourceName)
        assertEquals("Alice", relay.title)
        assertEquals("Llamada entrante", relay.text)
    }

    @Test
    fun `relays whatsapp business voip channel`() {
        val decision = CallFilter.decide(
            notification(
                packageName = "com.whatsapp.w4b",
                channelId = "incoming_call_001",
                title = "Bob",
                text = "Videollamada",
            )
        )
        assertTrue(decision is RelayDecision.Relay)
        val relay = decision as RelayDecision.Relay
        assertEquals("whatsapp", relay.sourceId)
    }

    @Test
    fun `relays by category call when channel unknown`() {
        val decision = CallFilter.decide(
            notification(
                channelId = "unknown_channel_xyz",
                category = Notification.CATEGORY_CALL,
                title = "Carlos",
                text = "Llamada",
            )
        )
        assertTrue(decision is RelayDecision.Relay)
    }

    @Test
    fun `ignores disabled source`() {
        // CallSourceRegistry only has enabled sources, so a package matching
        // a hypothetically disabled source would not be found. This test
        // verifies that an unknown package (simulating disabled) is ignored.
        val decision = CallFilter.decide(
            notification(
                packageName = "org.telegram.messenger",
                channelId = "call_channel",
                category = Notification.CATEGORY_CALL,
            )
        )
        assertTrue(decision is RelayDecision.Ignore)
    }

    @Test
    fun `passes null actions through to relay decision`() {
        val decision = CallFilter.decide(
            notification(channelId = "voip_channel", title = "Alice", text = "Llamada", actions = null)
        )
        assertTrue(decision is RelayDecision.Relay)
        val relay = decision as RelayDecision.Relay
        assertNull(relay.actions)
    }

    @Test
    fun `uses source displayName when notification title is null`() {
        val decision = CallFilter.decide(
            notification(channelId = "voip_notifications", title = null, text = "Llamada")
        )
        assertTrue(decision is RelayDecision.Relay)
        val relay = decision as RelayDecision.Relay
        assertEquals("WhatsApp", relay.title)
    }
}
