package com.wada811.pubsubmessenger.sample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.wada811.pubsubmessenger.PubSubMessage
import com.wada811.pubsubmessenger.messenger.pubSubMessenger
import com.wada811.pubsubmessenger.sample.Screen2Message.LaunchScreen1
import com.wada811.pubsubmessenger.sample.Screen2Message.Logging
import com.wada811.pubsubmessenger.subscribe
import java.util.Timer
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

class Screen2Activity : AppCompatActivity() {
    private val viewModel: Screen2ViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pubSubMessenger.subscribe<Logging> {
            Log.v("wada811", "Screen2.Logging: 1 ${lifecycle.currentState}")
        }
        pubSubMessenger.subscribe<Logging> {
            Log.v("wada811", "Screen2.Logging: 2 ${lifecycle.currentState}")
        }
        pubSubMessenger.subscribe<LaunchScreen1> {
            Log.d("wada811", "Screen2.LaunchScreen1")
            startActivity(Intent(this, Screen1Activity::class.java))
            finish()
        }
        viewModel.publish()
    }
}

class Screen2ViewModel : ViewModel() {
    lateinit var timer1: Timer
    lateinit var timer2: Timer
    fun publish() {
        timer1 = timer(period = TimeUnit.SECONDS.toMillis(5)) {
            Log.d("wada811", "Screen2.publish(): timer1")
            pubSubMessenger.publish(Logging)
        }
        timer2 = timer(initialDelay = TimeUnit.SECONDS.toMillis(10), period = TimeUnit.SECONDS.toMillis(10)) {
            Log.d("wada811", "Screen2.publish(): timer2")
            pubSubMessenger.publish(LaunchScreen1)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("wada811", "Screen2.onCleared")
        timer1.cancel()
        timer2.cancel()
    }
}

sealed class Screen2Message : PubSubMessage {
    object Logging : Screen2Message()
    object LaunchScreen1 : Screen2Message()
}
