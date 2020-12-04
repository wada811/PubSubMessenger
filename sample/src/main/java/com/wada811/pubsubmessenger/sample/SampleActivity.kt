package com.wada811.pubsubmessenger.sample

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wada811.databinding.dataBinding
import com.wada811.pubsubmessenger.PubSubMessage
import com.wada811.pubsubmessenger.publishMessage
import com.wada811.pubsubmessenger.sample.SampleViewModel.RestartMessage
import com.wada811.pubsubmessenger.sample.SampleViewModel.RotateMessage
import com.wada811.pubsubmessenger.sample.databinding.SampleActivityBinding
import com.wada811.pubsubmessenger.subscribeMessage

class SampleActivity : AppCompatActivity(R.layout.sample_activity) {
    private val binding: SampleActivityBinding by dataBinding()
    private val viewModel: SampleViewModel by viewModels()
    private var message: PubSubMessage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.appendLog("Activity::onCreate")
        binding.viewModel = viewModel
        binding.rotateButton.setOnClickListener {
            viewModel.appendLog("Activity::rotate")
            message = RotateMessage(requestedOrientation)
            requestedOrientation = when (requestedOrientation) {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                else -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
        subscribeMessage<RotateMessage> {
            viewModel.appendLog("Activity::rotated: ${it.requestedOrientation}")
        }
        subscribeMessage<RotateMessage> {
            println("subscribeMessage: $it")
        }
        binding.restartButton.setOnClickListener {
            if (isEnabledDoNotKeepActivities()) {
                val restartTimeMillis = System.currentTimeMillis()
                viewModel.appendLog("Activity::restart: $restartTimeMillis")
                message = RestartMessage(restartTimeMillis)
                startActivity(MainActivity.createIntent(this))
            } else {
                Toast.makeText(this, "adb shell settings put global always_finish_activities 1", Toast.LENGTH_LONG).show()
            }
        }
        subscribeMessage<RestartMessage> {
            viewModel.appendLog("Activity::restart: ${it.restartTimeMillis}, restarted: ${System.currentTimeMillis()}")
        }
    }

    private fun isEnabledDoNotKeepActivities(): Boolean {
        val doNotKeepActivities = Settings.Global.getInt(contentResolver, Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0)
        return doNotKeepActivities != 0
    }

    override fun onStart() {
        super.onStart()
        viewModel.appendLog("Activity::onStart")
    }

    override fun onResume() {
        super.onResume()
        viewModel.appendLog("Activity::onResume")
    }

    override fun onPause() {
        super.onPause()
        viewModel.appendLog("Activity::onPause")
    }

    override fun onStop() {
        super.onStop()
        viewModel.appendLog("Activity::onStop")
        message?.let {
            when (it) {
                is RotateMessage -> {
                    viewModel.appendLog("onRotate: ${it.requestedOrientation}")
                    viewModel.publishMessage(it)
                }
                is RestartMessage -> {
                    viewModel.appendLog("onRestart: ${it.restartTimeMillis}")
                    viewModel.publishMessage(it)
                }
                else -> Unit
            }
        }
        message = null
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.appendLog("Activity::onDestroy")
    }
}

