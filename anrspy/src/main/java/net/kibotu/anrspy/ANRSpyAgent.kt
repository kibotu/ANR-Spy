package net.kibotu.anrspy

import android.os.Handler
import android.os.Looper

class ANRSpyAgent(
    private var shouldThrowException: Boolean = false,
    private var timeout: Long = 5000L,
    private val interval: Long = 500L,
    private val handler: Handler = Handler(Looper.getMainLooper()),
    private var timeWaited: Long = 0L,
    private var onWait: ((ms: Long) -> Unit)? = null,
    private var onAnrDetected: ((exception: ANRSpyException) -> Unit)? = null
) : Thread() {

    private val testerWorker: Runnable = Runnable { timeWaited = 0L }

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
            shouldThrowException = shouldThrowException,
            timeout = timeout
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