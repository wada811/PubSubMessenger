package com.wada811.pubsubmessenger.messenger

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.wada811.pubsubmessenger.PublishMessenger
import com.wada811.pubsubmessenger.SubscribeMessenger
import kotlinx.coroutines.CoroutineScope

class ViewMessenger(
    private val view: View,
    override val viewModelStoreOwner: ViewModelStoreOwner = view.findViewTreeViewModelStoreOwner()!!,
    override val lifecycle: Lifecycle = view.findViewTreeLifecycleOwner()!!.lifecycle,
    override val coroutineScope: CoroutineScope = lifecycle.coroutineScope,
) : PublishMessenger, SubscribeMessenger

val View.pubSubMessenger: ViewMessenger
    get() = ViewMessenger(this)