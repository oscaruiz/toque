package app.toque

import java.util.concurrent.atomic.AtomicInteger

object IdGen {
    private val counter = AtomicInteger(1)
    fun next(): Int = counter.getAndIncrement()
}
