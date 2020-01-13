package com.wada811.pubsubmessenger.sample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wada811.pubsubmessenger.PubSubMessage
import com.wada811.viewmodelsavedstate.liveData

class SampleViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    val log: MutableLiveData<String> by savedStateHandle.liveData()

    init {
        appendLog("ViewModel::init")
    }

    fun appendLog(text: String) {
        println("appendLog: $text")
        val maxLineCount = 20
        val logLines = log.value!!.split("\n")
        if (logLines.size > maxLineCount) {
            log.value = logLines.subList(logLines.size - maxLineCount + 1, logLines.size).joinToString("\n")
        }
        log.value = log.value + "\n$text"
    }

    override fun onCleared() {
        super.onCleared()
        appendLog("ViewModel::onCleared")
    }

    data class RotateMessage(val requestedOrientation: Int) : PubSubMessage
    data class RestartMessage(val restartTimeMillis: Long) : PubSubMessage
}