package pk.farimarwat.anrspyexample

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import pk.farimarwat.anrspy.ANRSpyAgent
import pk.farimarwat.anrspy.ANRSpyListener
import pk.farimarwat.anrspyexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "ANR Spy"
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    lateinit var receiver: AirPlanMode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        receiver = AirPlanMode()
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(receiver, it)
        }
        val anrSpyAgent = ANRSpyAgent.Builder()
            .setSpyListener(object : ANRSpyListener {
                override fun onWait(ms: Long) {
                    //Log.e(TAG,"Waited: $ms")
                }

                override fun onAnrStackTrace(stacksTrace: Array<StackTraceElement>) {
                    Log.e(TAG, "Stack:\n $stacksTrace")
                }

                override fun onAnrDetected(details: String, stackTrace: Array<StackTraceElement>) {
                    Log.e(TAG, details)
                    Log.e(TAG, "$stackTrace")
                }
            })
            .setThrowException(true)
            .setTimeOut(5000)
            .build()
        anrSpyAgent.start()
        initGui()
    }

    fun initGui() {
        binding.btnMain.setOnClickListener {
            for (i in 1..10) {
                Log.e(TAG, "Number: $i")
                Thread.sleep(1000)
            }
        }
        binding.btnService.setOnClickListener {
            startService(Intent(this, MyService::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}