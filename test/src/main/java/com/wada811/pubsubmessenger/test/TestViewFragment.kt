package com.wada811.pubsubmessenger.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import com.wada811.pubsubmessenger.publishMessage
import com.wada811.pubsubmessenger.subscribeMessage

class TestViewFragment : Fragment() {
    companion object {
        fun createBundle(
            subscribeWhenLifecycleEvents: List<Lifecycle.Event>,
            publishWhenLifecycleEvents: List<Lifecycle.Event>
        ) = Bundle().also {
            it.putIntArray(TestViewFragment::subscribeWhenLifecycleEvents.name, subscribeWhenLifecycleEvents.map(Lifecycle.Event::ordinal).toIntArray())
            it.putIntArray(TestViewFragment::publishWhenLifecycleEvents.name, publishWhenLifecycleEvents.map(Lifecycle.Event::ordinal).toIntArray())
        }
    }

    private val viewModel: TestViewModel by viewModels()
    val subscribeWhenLifecycleEvents: List<Lifecycle.Event> by lazy {
        requireArguments().getIntArray(this::subscribeWhenLifecycleEvents.name)!!.map { Lifecycle.Event.values()[it] }.toList()
    }
    val publishWhenLifecycleEvents: List<Lifecycle.Event> by lazy {
        requireArguments().getIntArray(this::publishWhenLifecycleEvents.name)!!.map { Lifecycle.Event.values()[it] }.toList()
    }
    val lifecycleEvents: MutableList<Lifecycle.Event> = mutableListOf()

    private fun subscribeAndPublish(event: Lifecycle.Event) {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    override fun onDestroyView() {
        super.onDestroyView()
        subscribeAndPublish(ON_DESTROY)
    }
}