package net.kibotu.anrspy.demo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

class MyService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        for (i in 0..10) {
            Timber.v("Number: $i")
            Thread.sleep(1000)
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}