package com.wada811.pubsubmessenger.test

import androidx.lifecycle.Lifecycle
import com.wada811.pubsubmessenger.PubSubMessage

data class TestMessage(val lifecycleEvent: Lifecycle.Event) : PubSubMessage
