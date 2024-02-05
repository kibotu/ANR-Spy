package net.kibotu.anrspy.demo

import android.app.Application
import net.kibotu.anrspy.startSpying
import timber.log.Timber
import java.time.Clock
import java.time.Duration
import java.time.Instant

class App : Application() {

    val now: Instant
        get() = Clock.systemDefaultZone().instant()

    private val startTime = now

    override fun onCreate() {

        Timber.plant(Timber.DebugTree())
        logSinceStart()

        startSpying {

            shouldThrowException = false
            timeout = 5000L

//            onWait {
//                Log.e(TAG, "Waited: $it")
//            }

            onAnrDetected {
                logSinceStart()
                Timber.e(it)
            }
        }

        super.onCreate()
    }

    private fun logSinceStart() {
        Timber.tag("ANR-SPY-dt").v("dt=${Duration.between(startTime, now).toMillis()}")
    }
}