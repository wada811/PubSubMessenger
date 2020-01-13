package com.wada811.pubsubmessenger.test

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import com.wada811.pubsubmessenger.subscribeMessage

class TestDialogActivity : FragmentActivity() {
    companion object {
        fun createIntent(
            subscribeWhenLifecycleEvents: List<Event>,
            publishWhenLifecycleEvents: List<Event>
        ) = Intent("com.wada811.pubsubmessenger.test.ACTION_DIALOG").also {
            it.putExtra(TestDialogActivity::subscribeWhenLifecycleEvents.name, subscribeWhenLifecycleEvents.map(Event::ordinal).toIntArray())
            it.putExtra(TestDialogActivity::publishWhenLifecycleEvents.name, publishWhenLifecycleEvents.map(Event::ordinal).toIntArray())
        }
    }

    val subscribeWhenLifecycleEvents: List<Event> by lazy {
        intent.getIntArrayExtra(this::subscribeWhenLifecycleEvents.name)!!.map { Event.values()[it] }.toList()
    }
    val publishWhenLifecycleEvents: List<Event> by lazy {
        intent.getIntArrayExtra(this::publishWhenLifecycleEvents.name)!!.map { Event.values()[it] }.toList()
    }
    val lifecycleEvents: MutableList<Event> = mutableListOf()

    private fun subscribe(event: Event) {
        println("state: ${lifecycle.currentState}, event: $event")
        subscribeWhenLifecycleEvents.filter { it == event }.forEach { _ ->
            println("subscribe: $event")
            subscribeMessage<TestMessage> {
                println("lifecycleEvents.add: ${it.lifecycleEvent}")
                lifecycleEvents.add(it.lifecycleEvent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TestDialogFragment().also {
            it.arguments = TestDialogFragment.createBundle(publishWhenLifecycleEvents)
        }.show(supportFragmentManager, TestDialogFragment::class.java.simpleName)
        subscribe(ON_CREATE)
    }

    override fun onStart() {
        super.onStart()
        subscribe(ON_START)
    }

    override fun onResume() {
        super.onResume()
        subscribe(ON_RESUME)
    }

    override fun onPause() {
        super.onPause()
        subscribe(ON_PAUSE)
    }

    override fun onStop() {
        super.onStop()
        subscribe(ON_STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscribe(ON_DESTROY)
    }
}