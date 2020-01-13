package com.wada811.pubsubmessenger.test

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import com.wada811.pubsubmessenger.publishMessage

class TestDialogFragment : DialogFragment() {
    companion object {
        fun createBundle(
            publishWhenLifecycleEvents: List<Event>
        ) = Bundle().also {
            it.putIntArray(TestDialogFragment::publishWhenLifecycleEvents.name, publishWhenLifecycleEvents.map(Event::ordinal).toIntArray())
        }
    }

    val publishWhenLifecycleEvents: List<Event> by lazy {
        requireArguments().getIntArray(this::publishWhenLifecycleEvents.name)!!.map { Event.values()[it] }.toList()
    }

    private fun publish(event: Event) {
        println("state: ${if (view != null) viewLifecycleOwner.lifecycle.currentState else lifecycle.currentState}, event: $event")
        publishWhenLifecycleEvents.filter { it == event }.forEach { _ ->
            println("publish: $event")
            publishMessage(TestMessage(event))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        publish(ON_CREATE)
        return Dialog(requireContext())
    }

    override fun onStart() {
        super.onStart()
        publish(ON_START)
    }

    override fun onResume() {
        super.onResume()
        publish(ON_RESUME)
    }

    override fun onPause() {
        super.onPause()
        publish(ON_PAUSE)
    }

    override fun onStop() {
        super.onStop()
        publish(ON_STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        publish(ON_DESTROY)
    }
}