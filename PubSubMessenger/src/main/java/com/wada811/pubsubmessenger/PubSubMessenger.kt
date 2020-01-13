package com.wada811.pubsubmessenger

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED

@OptIn(ExperimentalCoroutinesApi::class)
internal object PubSubMessenger {
    private val channel: BroadcastChannel<PubSubMessage> = BroadcastChannel(BUFFERED)

    internal suspend fun publishMessage(message: PubSubMessage) {
        channel.send(message)
    }

    internal fun <T : PubSubMessage> subscribeMessage(
        lifecycle: Lifecycle,
        clazz: Class<T>,
        onReceive: (T) -> Unit
    ): Job {
        val receiveChannel = channel.openSubscription()
        if (lifecycle.currentState == DESTROYED) {
            receiveChannel.cancel()
        } else {
            lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Event) {
                    if (event == ON_DESTROY) {
                        receiveChannel.cancel()
                    }
                }
            })
        }
        return lifecycle.coroutineScope.launchWhenStarted {
            for (message in receiveChannel) {
                lifecycle.coroutineScope.launchWhenStarted {
                    if (message::class.java == clazz) {
                        @Suppress("UNCHECKED_CAST")
                        onReceive(message as T)
                    }
                }
            }
        }
    }
}