package pk.farimarwat.anrspy

import android.os.Handler
import android.os.Looper

class ANRSpyAgent constructor(builder: Builder) : Thread() {

    private val title = "[ ++ ANR Spy ++ ]"

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

        fun getSpyListener() = this.listener

        fun setThrowException(throwexception: Boolean) =
            apply { this.shouldThrowException = throwexception }

        fun getThrowException() = this.shouldThrowException

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
                    "$title Main thread blocked for: $timeWaited ms",
                    Looper.getMainLooper().thread.stackTrace
                )
                if (shouldThrowException) {
                    throwException(Looper.getMainLooper().thread.stackTrace)
                }
            }
        }
    }

    private fun throwException(stackTrace: Array<StackTraceElement>) {
        throw ANRSpyException(title, stackTrace)
    }
}