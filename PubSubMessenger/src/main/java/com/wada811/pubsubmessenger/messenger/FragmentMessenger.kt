package com.wada811.pubsubmessenger.messenger

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.coroutineScope
import com.wada811.pubsubmessenger.PublishMessenger
import com.wada811.pubsubmessenger.SubscribeMessenger
import kotlinx.coroutines.CoroutineScope

class FragmentMessenger(
    private val fragment: Fragment,
    override val viewModelStoreOwner: ViewModelStoreOwner = fragment,
    override val lifecycle: Lifecycle = fragment.viewLifecycleOrLifecycle,
    override val coroutineScope: CoroutineScope = lifecycle.coroutineScope,
) : PublishMessenger, SubscribeMessenger

private val Fragment.viewLifecycleOrLifecycle: Lifecycle
    get() = if (viewLifecycleOwnerLiveData.value != null) viewLifecycleOwner.lifecycle else lifecycle

val Fragment.pubSubMessenger: FragmentMessenger
    get() = FragmentMessenger(this)