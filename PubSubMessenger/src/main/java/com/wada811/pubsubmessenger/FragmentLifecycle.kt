package com.wada811.pubsubmessenger

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

internal val Fragment.viewLifecycleOrLifecycle: Lifecycle
    get() = if (viewLifecycleOwnerLiveData.value != null) viewLifecycleOwner.lifecycle else lifecycle
