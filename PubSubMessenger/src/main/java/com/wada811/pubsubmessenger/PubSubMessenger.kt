package com.wada811.pubsubmessenger

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

interface PubSubMessage

interface PublishMessenger {
    val coroutineScope: CoroutineScope
    fun publish(message: PubSubMessage) {
        coroutineScope.launch {
            PubSubMessenger.publish(message)
        }
    }
}

interface SubscribeMessenger {
    val viewModelStoreOwner: ViewModelStoreOwner
    val lifecycle: Lifecycle

    @Deprecated("use inline fun", ReplaceWith("this.subscribe<T>(onReceive)"), level = DeprecationLevel.WARNING)
    fun <T : PubSubMessage> subscribe(clazz: Class<T>, onReceive: (T) -> Unit): Job {
        val viewModel = LifecycleViewModel.get(viewModelStoreOwner, "${clazz.name}-$onReceive", lifecycle)
        return PubSubMessenger.subscribe(viewModel.lifecycleScope, clazz, onReceive)
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : PubSubMessage> SubscribeMessenger.subscribe(noinline onReceive: (T) -> Unit): Job {
    return subscribe(T::class.java, onReceive)
}

internal object PubSubMessenger {
    private val flow: MutableSharedFlow<PubSubMessage> = MutableSharedFlow()
    suspend fun publish(message: PubSubMessage) = flow.emit(message)
    fun <T : PubSubMessage> subscribe(coroutineScope: LifecycleCoroutineScope, clazz: Class<T>, onReceive: (T) -> Unit): Job {
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
