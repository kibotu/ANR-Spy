package net.kibotu.anrspy

import android.os.Handler
import android.os.Looper

class ANRSpyAgent constructor(builder: Builder) : Thread() {

    private var listener: ANRSpyListener? = builder.getSpyListener()

    private var shouldThrowException: Boolean = builder.getThrowException()

    private var timeout = builder.getTimeOut()

    private val interval = 500L

    private val handler = Handler(Looper.getMainLooper())

    private var timeWaited = 0L

    private val testerWorker = Runnable {
        timeWaited = 0L
    }

    class Builder {

        //Params
        private var listener: ANRSpyListener? = null

        private var shouldThrowException: Boolean = true

        private var timeout = 5000L

        fun setSpyListener(listener: ANRSpyListener) = apply { this.listener = listener }

        fun getSpyListener() = listener

        fun setThrowException(throwException: Boolean) =
            apply { shouldThrowException = throwException }

        fun getThrowException() = shouldThrowException

        fun setTimeOut(timeout: Long) = apply { this.timeout = timeout }

        fun getTimeOut() = timeout

        fun build(): ANRSpyAgent = ANRSpyAgent(this)
    }
    //End builder

    override fun run() {
        while (!isInterrupted) {
            timeWaited += interval
            listener?.onWait(timeWaited)
            handler.post(testerWorker)
            sleep(interval)
            if (timeWaited > timeout) {
                listener?.onAnrDetected(
                    "$[ANR Spy] Main thread blocked for: $timeWaited ms",
                    Looper.getMainLooper().thread.stackTrace
                )
                if (shouldThrowException) {
                    throw ANRSpyException("Application Not Responding for at least $timeWaited ms.", Looper.getMainLooper().thread.stackTrace)
                }
            }
        }
    }
}