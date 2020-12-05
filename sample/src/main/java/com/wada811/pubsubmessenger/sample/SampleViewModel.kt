package com.wada811.pubsubmessenger.sample

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.wada811.pubsubmessenger.PubSubMessage
import com.wada811.pubsubmessenger.messenger.pubSubMessenger

class SampleViewModel(application: Application) : AndroidViewModel(application) {
    private val pref = application.getSharedPreferences(application.getString(R.string.app_name), Context.MODE_PRIVATE)
    val log: MutableLiveData<String> = MutableLiveData(pref.getString(::log.name, ""))

    init {
        appendLog("ViewModel::init")
    }

    fun publishMessage(message: PubSubMessage) {
        pubSubMessenger.publish(message)
    }

    fun appendLog(text: String) {
        println("appendLog: $text")
        val maxLineCount = 20
        val logLines = log.value!!.split("\n")
        if (logLines.size > maxLineCount) {
            log.value = logLines.subList(logLines.size - maxLineCount + 1, logLines.size).joinToString("\n")
        }
        log.value = log.value + "\n$text"
        pref.edit { putString(::log.name, log.value) }
    }

    override fun onCleared() {
        super.onCleared()
        appendLog("ViewModel::onCleared")
    }

    data class RotateMessage(val requestedOrientation: Int) : PubSubMessage
    data class RestartMessage(val restartTimeMillis: Long) : PubSubMessage
}
