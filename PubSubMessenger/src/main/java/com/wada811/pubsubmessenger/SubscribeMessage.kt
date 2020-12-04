@file:Suppress("DEPRECATION")

package com.wada811.pubsubmessenger

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.Job
import kotlin.DeprecationLevel.WARNING

@Deprecated("use inline fun", ReplaceWith("this.subscribeMessage<T>(onReceive)"), level = WARNING)
fun <T : PubSubMessage> FragmentActivity.subscribeMessage(clazz: Class<T>, onReceive: (T) -> Unit): Job {
    return SubscribeViewModel.subscribeMessage(this, lifecycle, clazz, onReceive)
}

inline fun <reified T : PubSubMessage> FragmentActivity.subscribeMessage(noinline onReceive: (T) -> Unit): Job {
    return subscribeMessage(T::class.java, onReceive)
}

@Deprecated("use inline fun", ReplaceWith("this.subscribeMessage<T>(onReceive)"), level = WARNING)
fun <T : PubSubMessage> Fragment.subscribeMessage(clazz: Class<T>, onReceive: (T) -> Unit): Job {
    return SubscribeViewModel.subscribeMessage(this, viewLifecycleOrLifecycle, clazz, onReceive)
}

inline fun <reified T : PubSubMessage> Fragment.subscribeMessage(noinline onReceive: (T) -> Unit): Job {
    return subscribeMessage(T::class.java, onReceive)
}
