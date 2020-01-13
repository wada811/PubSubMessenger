package com.wada811.pubsubmessenger.test

import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.test.core.app.ActivityScenario
import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(ParameterizedRobolectricTestRunner::class)
class DialogActivityTest(
    private val subscribeLifecycleEvents: List<Event>,
    private val publishLifecycleEvents: List<Event>,
    private val expectedLifecycleEvents: List<Event>
) {
    @Test
    fun test() {
        var activity: TestDialogActivity? = null
        ActivityScenario.launch<TestDialogActivity>(TestDialogActivity.createIntent(
            subscribeLifecycleEvents,
            publishLifecycleEvents
        )).use { scenario ->
            scenario.onActivity { activity = it }
            scenario.moveToState(Lifecycle.State.DESTROYED)
            Truth.assertThat(activity!!.lifecycleEvents).containsExactly(*expectedLifecycleEvents.toTypedArray()).inOrder()
            activity = null
        }
    }

    companion object {
        @Suppress("unused")
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "subscribe: {0}, publish: {1}, expected: {2}")
        fun parameters(): Iterable<Array<Any>> {
            val parameters = mutableListOf<Array<Any>>()
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_CREATE), listOf(ON_CREATE)))
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_START), listOf(ON_START)))
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_RESUME), listOf(ON_RESUME)))
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_PAUSE), listOf(ON_PAUSE)))
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_STOP), listOf<Event>())) // publish after ON_STOP
            parameters.add(arrayOf(listOf(ON_CREATE), listOf(ON_DESTROY), listOf<Event>())) // publish after ON_STOP
            return parameters
        }
    }
}