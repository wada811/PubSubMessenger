package com.wada811.pubsubmessenger

import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal object PubSubMessenger {
    private val flow: MutableSharedFlow<PubSubMessage> = MutableSharedFlow()
    internal suspend fun publishMessage(message: PubSubMessage) {
        flow.emit(message)
    }

    internal fun <T : PubSubMessage> subscribeMessage(
        coroutineScope: LifecycleCoroutineScope,
        clazz: Class<T>,
        onReceive: (T) -> Unit
    ): Job {
        return flow
            .filter { clazz.isInstance(it) }
            .onEach {
                coroutineScope.launchWhenStarted {
                    @Suppress("UNCHECKED_CAST")
                    onReceive(it as T)
                }
            }
            .launchIn(coroutineScope)
    }
}
