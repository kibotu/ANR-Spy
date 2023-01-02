package net.kibotu.anrspy

import android.os.Handler
import android.os.Looper

class ANRSpyAgent(
    private val interval: Long = 500L,
    private var timeout: Long = 5000L,
    private var shouldThrowException: Boolean = false,
    private var onWait: ((ms: Long) -> Unit)? = null,
    private var onAnrDetected: ((exception: ANRSpyException) -> Unit)? = null
) : Thread() {

    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    private val testerWorker: Runnable = Runnable { timeWaited = 0L }

    private var timeWaited: Long = 0L

    override fun run() {
        while (!isInterrupted) {
            timeWaited += interval
            onWait?.invoke(timeWaited)
            handler.post(testerWorker)
            sleep(interval)
            if (timeWaited > timeout) {
                val exception = ANRSpyException(
                    "Application Not Responding for at least $timeWaited ms.",
                    Looper.getMainLooper().thread.stackTrace
                )
                onAnrDetected?.invoke(exception)
                if (shouldThrowException) {
                    throw exception
                }
            }
        }
    }

    // region Builder

    class Builder {

        var shouldThrowException: Boolean = true

        var timeout = 5000L

        var interval = 500L

        private var onWait: ((ms: Long) -> Unit)? = null

        private var onAnrDetected: ((exception: ANRSpyException) -> Unit)? =
            null

        fun onWait(block: ((ms: Long) -> Unit)? = null) {
            onWait = block
        }

        fun onAnrDetected(block: ((exception: ANRSpyException) -> Unit)? = null) {
            onAnrDetected = block
        }

        fun build(): ANRSpyAgent = ANRSpyAgent(
            interval = interval,
            timeout = timeout,
            shouldThrowException = shouldThrowException,
            onWait = onWait,
            onAnrDetected = onAnrDetected
        )
    }

    // endregion
}

fun startSpying(update: ANRSpyAgent.Builder.() -> Unit) {
    val builder = ANRSpyAgent.Builder()
    builder.update()
    val thread = builder.build()
    thread.start()
}