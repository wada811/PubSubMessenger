package com.wada811.pubsubmessenger.test

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.Lifecycle.Event.*
import com.wada811.pubsubmessenger.publishMessage
import com.wada811.pubsubmessenger.subscribeMessage

class TestNoViewFragment : Fragment() {
    companion object {
        fun createBundle(
            subscribeWhenLifecycleEvents: List<Event>,
            publishWhenLifecycleEvents: List<Event>
        ) = Bundle().also {
            it.putIntArray(TestNoViewFragment::subscribeWhenLifecycleEvents.name, subscribeWhenLifecycleEvents.map(Event::ordinal).toIntArray())
            it.putIntArray(TestNoViewFragment::publishWhenLifecycleEvents.name, publishWhenLifecycleEvents.map(Event::ordinal).toIntArray())
        }
    }

    private val viewModel: TestViewModel by viewModels()
    val subscribeWhenLifecycleEvents: List<Event> by lazy {
        requireArguments().getIntArray(this::subscribeWhenLifecycleEvents.name)!!.map { Event.values()[it] }.toList()
    }
    val publishWhenLifecycleEvents: List<Event> by lazy {
        requireArguments().getIntArray(this::publishWhenLifecycleEvents.name)!!.map { Event.values()[it] }.toList()
    }
    val lifecycleEvents: MutableList<Event> = mutableListOf()

    private fun subscribeAndPublish(event: Event) {
        println("state: ${if (view != null) viewLifecycleOwner.lifecycle.currentState else lifecycle.currentState}, event: $event")
        subscribeWhenLifecycleEvents.filter { it == event }.forEach { _ ->
            println("subscribe: $event")
            subscribeMessage<TestMessage> {
                println("lifecycleEvents.add: $it")
                lifecycleEvents.add(it.lifecycleEvent)
            }
        }
        publishWhenLifecycleEvents.filter { it == event }.forEach { _ ->
            println("publish: $event")
            viewModel.publishMessage(TestMessage(event))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeAndPublish(ON_CREATE)
    }

    override fun onStart() {
        super.onStart()
        subscribeAndPublish(ON_START)
    }

    override fun onResume() {
        super.onResume()
        subscribeAndPublish(ON_RESUME)
    }

    override fun onPause() {
        super.onPause()
        subscribeAndPublish(ON_PAUSE)
    }

    override fun onStop() {
        super.onStop()
        subscribeAndPublish(ON_STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscribeAndPublish(ON_DESTROY)
    }
}