package com.wada811.pubsubmessenger.test

import android.os.Build
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.Lifecycle.State
import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(ParameterizedRobolectricTestRunner::class)
class NoViewFragmentTest(
    private val subscribeLifecycleEvents: List<Event>,
    private val publishLifecycleEvents: List<Event>,
    private val expectedLifecycleEvents: List<Event>
) {
    @Test
    fun test() {
        var fragment: TestNoViewFragment? = null
        val scenario = FragmentScenario.launch(
            TestNoViewFragment::class.java,
            TestNoViewFragment.createBundle(
                subscribeLifecycleEvents,
                publishLifecycleEvents
            )
        )
        scenario.onFragment { fragment = it }
        scenario.moveToState(State.DESTROYED)
        Truth.assertThat(fragment!!.lifecycleEvents).containsExactly(*expectedLifecycleEvents.toTypedArray()).inOrder()
        fragment = null
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "subscribe: {0}, publish: {1}, expected: {2}")
        fun parameters(): Iterable<Array<Any>> {
            val parameters = mutableListOf<Array<Any>>()
            // subscribe: ON_CREATE
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_CREATE), listOf(ON_CREATE)))
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_START), listOf(ON_START)))
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_RESUME), listOf(ON_RESUME)))
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_PAUSE), listOf(ON_PAUSE)))
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_STOP), listOf<Event>())) // publish after ON_STOP
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_DESTROY), listOf<Event>())) // publish after ON_STOP
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_CREATE, ON_CREATE), listOf(ON_CREATE, ON_CREATE)))
            parameters.add(arrayOf(listOf(ON_CREATE, ON_CREATE), listOf(ON_CREATE), listOf(ON_CREATE, ON_CREATE)))

            // subscribe: ON_START
            parameters.add(arrayOf(listOf(ON_START), listOf(ON_CREATE), listOf<Event>())) // publish before subscribe
            parameters.add(arrayOf(listOf(ON_START), listOf(ON_START), listOf(ON_START)))
            parameters.add(arrayOf(listOf(ON_START), listOf(ON_RESUME), listOf(ON_RESUME)))
            parameters.add(arrayOf(listOf(ON_START), listOf(ON_PAUSE), listOf(ON_PAUSE)))
            parameters.add(arrayOf(listOf(ON_START), listOf(ON_STOP), listOf<Event>())) // publish after ON_STOP
            parameters.add(arrayOf(listOf(ON_START), listOf(ON_DESTROY), listOf<Event>())) // publish after ON_STOP
            parameters.add(arrayOf(listOf(ON_START), listOf(ON_START, ON_START), listOf(ON_START, ON_START)))
            parameters.add(arrayOf(listOf(ON_START, ON_START), listOf(ON_START), listOf(ON_START, ON_START)))

            // subscribe: ON_RESUME
            parameters.add(arrayOf(listOf(ON_RESUME), listOf(ON_CREATE), listOf<Event>())) // publish before subscribe
            parameters.add(arrayOf(listOf(ON_RESUME), listOf(ON_START), listOf<Event>())) // publish before subscribe
            parameters.add(arrayOf(listOf(ON_RESUME), listOf(ON_RESUME), listOf(ON_RESUME)))
            parameters.add(arrayOf(listOf(ON_RESUME), listOf(ON_PAUSE), listOf(ON_PAUSE)))
            parameters.add(arrayOf(listOf(ON_RESUME), listOf(ON_STOP), listOf<Event>())) // publish after ON_STOP
            parameters.add(arrayOf(listOf(ON_RESUME), listOf(ON_DESTROY), listOf<Event>())) // publish after ON_STOP
            parameters.add(arrayOf(listOf(ON_RESUME), listOf(ON_RESUME, ON_RESUME), listOf(ON_RESUME, ON_RESUME)))
            parameters.add(arrayOf(listOf(ON_RESUME, ON_RESUME), listOf(ON_RESUME), listOf(ON_RESUME, ON_RESUME)))

            // subscribe: ON_PAUSE
            parameters.add(arrayOf(listOf(ON_PAUSE), listOf(ON_CREATE), listOf<Event>())) // publish before subscribe
            parameters.add(arrayOf(listOf(ON_PAUSE), listOf(ON_START), listOf<Event>())) // publish before subscribe
            parameters.add(arrayOf(listOf(ON_PAUSE), listOf(ON_RESUME), listOf<Event>())) // publish before subscribe
            parameters.add(arrayOf(listOf(ON_PAUSE), listOf(ON_PAUSE), listOf(ON_PAUSE)))
            parameters.add(arrayOf(listOf(ON_PAUSE), listOf(ON_STOP), listOf<Event>())) // publish after ON_STOP
            parameters.add(arrayOf(listOf(ON_PAUSE), listOf(ON_DESTROY), listOf<Event>())) // publish after ON_STOP
            parameters.add(arrayOf(listOf(ON_PAUSE), listOf(ON_PAUSE, ON_PAUSE), listOf(ON_PAUSE, ON_PAUSE)))
            parameters.add(arrayOf(listOf(ON_PAUSE, ON_PAUSE), listOf(ON_PAUSE), listOf(ON_PAUSE, ON_PAUSE)))

            // subscribe: ON_STOP
            parameters.add(arrayOf(listOf(ON_STOP), listOf(ON_CREATE), listOf<Event>())) // subscribe after ON_STOP
            parameters.add(arrayOf(listOf(ON_STOP), listOf(ON_START), listOf<Event>())) // subscribe after ON_STOP
            parameters.add(arrayOf(listOf(ON_STOP), listOf(ON_RESUME), listOf<Event>())) // subscribe after ON_STOP
            parameters.add(arrayOf(listOf(ON_STOP), listOf(ON_PAUSE), listOf<Event>())) // subscribe after ON_STOP
            parameters.add(arrayOf(listOf(ON_STOP), listOf(ON_STOP), listOf<Event>())) // subscribe after ON_STOP
            parameters.add(arrayOf(listOf(ON_STOP), listOf(ON_DESTROY), listOf<Event>())) // subscribe after ON_STOP

            // subscribe: ON_DESTROY
            parameters.add(arrayOf(listOf(ON_DESTROY), listOf(ON_CREATE), listOf<Event>())) // subscribe after ON_STOP
            parameters.add(arrayOf(listOf(ON_DESTROY), listOf(ON_START), listOf<Event>())) // subscribe after ON_STOP
            parameters.add(arrayOf(listOf(ON_DESTROY), listOf(ON_RESUME), listOf<Event>())) // subscribe after ON_STOP
            parameters.add(arrayOf(listOf(ON_DESTROY), listOf(ON_PAUSE), listOf<Event>())) // subscribe after ON_STOP
            parameters.add(arrayOf(listOf(ON_DESTROY), listOf(ON_STOP), listOf<Event>())) // subscribe after ON_STOP
            parameters.add(arrayOf(listOf(ON_DESTROY), listOf(ON_DESTROY), listOf<Event>())) // subscribe after ON_STOP
            return parameters
        }
    }
}
