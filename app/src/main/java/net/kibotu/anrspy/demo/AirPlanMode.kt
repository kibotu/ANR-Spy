package net.kibotu.anrspy.demo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

class AirPlanMode : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        for (i in 0..10) {
            Timber.v("Number: $i")
            Thread.sleep(1000)
        }
    }
}