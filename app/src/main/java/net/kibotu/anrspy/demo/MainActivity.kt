package net.kibotu.anrspy.demo

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
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
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var receiver: AirPlanMode

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
                                Timber.v("Number: $i")
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

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
