package com.wada811.pubsubmessenger.messenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wada811.pubsubmessenger.PublishMessenger
import kotlinx.coroutines.CoroutineScope

class ViewModelMessenger(
    private val viewModel: ViewModel,
    override val coroutineScope: CoroutineScope = viewModel.viewModelScope
) : PublishMessenger

val ViewModel.pubSubMessenger: PublishMessenger
    get() = ViewModelMessenger(this)
