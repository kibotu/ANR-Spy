package net.kibotu.anrspy.demo

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import net.kibotu.anrspy.demo.theme.AppTheme

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "ANR Spy"
    }

    lateinit var receiver: AirPlanMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            for (i in 1..10) {
                                Log.e(TAG, "Number: $i")
                                Thread.sleep(30000)
                            }
                        }) {
                            Text(text = "Crash Main Thread by ANR")
                        }
                        Button(onClick = {
                            this@MainActivity.startService(
                                Intent(
                                    this@MainActivity,
                                    MyService::class.java
                                )
                            )
                        }) {
                            Text(text = "Crash Service by ANR")
                        }
                    }
                }
            }
        }

        receiver = AirPlanMode()
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(receiver, it)
        }

//        val anrSpyAgent = ANRSpyAgent.Builder()
//            .setSpyListener(object : ANRSpyListener {
//                override fun onWait(ms: Long) {
//                    //Log.e(TAG,"Waited: $ms")
//                }
//
//                override fun onAnrStackTrace(stacksTrace: Array<StackTraceElement>) {
//                    Log.e(TAG, "Stack:\n $stacksTrace")
//                }
//
//                override fun onAnrDetected(details: String, stackTrace: Array<StackTraceElement>) {
//                    Log.e(TAG, details)
//                    Log.e(TAG, "$stackTrace")
//                }
//            })
//            .setThrowException(true)
//            .setTimeOut(5000)
//            .build()
//        anrSpyAgent.start()

//        startSpying {
//
//            throwException = true
//            timeout = 5000L
//
//            onWait {
//                //Log.e(TAG,"Waited: $ms")
//            }
//
//            ononAnrStackTrace {
//                Log.e(TAG, "Stack:\n $stacksTrace")
//            }
//
//            onAnrDetected {
//                Log.e(TAG, details)
//                Log.e(TAG, "$stackTrace")
//            }
//        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
