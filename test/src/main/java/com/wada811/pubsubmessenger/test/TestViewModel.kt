package com.wada811.pubsubmessenger.test

import androidx.lifecycle.ViewModel
import com.wada811.pubsubmessenger.PubSubMessage
import com.wada811.pubsubmessenger.messenger.pubSubMessenger

class TestViewModel : ViewModel() {
    fun publishMessage(message: PubSubMessage) {
        pubSubMessenger.publish(message)
    }
}