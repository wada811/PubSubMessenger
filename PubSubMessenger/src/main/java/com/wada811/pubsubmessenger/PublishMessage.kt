package com.wada811.pubsubmessenger

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun Fragment.publishMessage(message: PubSubMessage): Job {
    return viewLifecycleOrLifecycle.coroutineScope.launch {
        PubSubMessenger.publishMessage(message)
    }
}

fun ViewModel.publishMessage(message: PubSubMessage): Job {
    return viewModelScope.launch {
        PubSubMessenger.publishMessage(message)
    }
}
