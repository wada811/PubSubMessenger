package com.wada811.pubsubmessenger.messenger

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStoreOwner
import com.wada811.pubsubmessenger.SubscribeMessenger

class ActivityMessenger(
    private val activity: FragmentActivity,
    override val viewModelStoreOwner: ViewModelStoreOwner = activity,
    override val lifecycle: Lifecycle = activity.lifecycle
) : SubscribeMessenger

val FragmentActivity.pubSubMessenger: ActivityMessenger
    get() = ActivityMessenger(this)