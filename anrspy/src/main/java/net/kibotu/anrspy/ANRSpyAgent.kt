package net.kibotu.anrspy

import android.os.Debug
import android.os.Handler
import android.os.Looper

class ANRSpyAgent(
    private val interval: Long = 500L,
    private var timeout: Long = 5000L,
    private var onWait: ((ms: Long) -> Unit)? = null,
    private var onAnrDetected: ((exception: ANRSpyException) -> Unit)? = null
) : Thread() {

    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    private val testerWorker: Runnable = Runnable { timeWaited = 0L }

    private var timeWaited: Long = 0L

    private val isDebuggerAttached: Boolean
        get() = Debug.isDebuggerConnected() || Debug.waitingForDebugger()

    @Volatile
    private var lastAnr: ANRSpyException? = null

    override fun run() {
        while (!isInterrupted) {
            timeWaited += interval
            onWait?.invoke(timeWaited)
            handler.post(testerWorker)
            sleep(interval)
            if (timeWaited > timeout) {

                // skip debugger
                if (isDebuggerAttached) continue

                val exception = ANRSpyException(
                    "Application Not Responding for at least $timeWaited ms.",
                    Looper.getMainLooper().thread.stackTrace
                )

                // avoid duplicated error propagation
                if (lastAnr?.stackTrace.contentDeepEquals(exception.stackTrace)) continue
                lastAnr = exception

                onAnrDetected?.invoke(exception)
                if (shouldThrowException) {
                    throw exception
                }
            }
        }
    }

    // region Builder

    class Builder {

        var shouldThrowException: Boolean
            get() = ANRSpyAgent.shouldThrowException
            set(value) {
                ANRSpyAgent.shouldThrowException = value
            }

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
            onWait = onWait,
            onAnrDetected = onAnrDetected
        )
    }

    // endregion

    companion object {

        @Volatile
        var shouldThrowException: Boolean = false
    }
}

fun startSpying(update: ANRSpyAgent.Builder.() -> Unit) {
    val builder = ANRSpyAgent.Builder()
    builder.update()
    val thread = builder.build()
    thread.start()
}