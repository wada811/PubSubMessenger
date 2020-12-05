package com.wada811.pubsubmessenger.test

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import com.wada811.pubsubmessenger.messenger.pubSubMessenger
import com.wada811.pubsubmessenger.subscribe

class TestActivity : FragmentActivity() {
    companion object {
        fun createIntent(
            subscribeWhenLifecycleEvents: List<Event>,
            publishWhenLifecycleEvents: List<Event>
        ) = Intent("com.wada811.pubsubmessenger.test.ACTION_TEST").also {
            it.putExtra(TestActivity::subscribeWhenLifecycleEvents.name, subscribeWhenLifecycleEvents.map(Event::ordinal).toIntArray())
            it.putExtra(TestActivity::publishWhenLifecycleEvents.name, publishWhenLifecycleEvents.map(Event::ordinal).toIntArray())
        }
    }

    private val viewModel: TestViewModel by viewModels()
    val subscribeWhenLifecycleEvents: List<Event> by lazy {
        intent.getIntArrayExtra(this::subscribeWhenLifecycleEvents.name)!!.map { Event.values()[it] }.toList()
    }
    val publishWhenLifecycleEvents: List<Event> by lazy {
        intent.getIntArrayExtra(this::publishWhenLifecycleEvents.name)!!.map { Event.values()[it] }.toList()
    }
    val lifecycleEvents: MutableList<Event> = mutableListOf()

    private fun subscribeAndPublish(event: Event) {
        println("state: ${lifecycle.currentState}, event: $event")
        subscribeWhenLifecycleEvents.filter { it == event }.forEach { _ ->
            println("subscribe: $event")
            pubSubMessenger.subscribe<TestMessage> {
                println("lifecycleEvents.add: ${it.lifecycleEvent}")
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
