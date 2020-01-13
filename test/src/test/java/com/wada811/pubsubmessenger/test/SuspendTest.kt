package com.wada811.pubsubmessenger.test

import android.os.Build
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.*
import androidx.test.core.app.ActivityScenario
import com.google.common.truth.Truth
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class SuspendTest {
    @Test
    fun activity_suspend_resume() {
        var activity: TestActivity? = null
        ActivityScenario.launch<TestActivity>(TestActivity.createIntent(
            listOf(ON_CREATE),
            listOf(ON_RESUME, ON_STOP)
        )).use { scenario ->
            scenario.onActivity { activity = it }
            Truth.assertThat(activity!!.lifecycleEvents).containsExactly(ON_RESUME).inOrder()
            scenario.moveToState(Lifecycle.State.CREATED)
            Truth.assertThat(activity!!.lifecycleEvents).containsExactly(ON_RESUME).inOrder()
            scenario.moveToState(Lifecycle.State.RESUMED)
            Truth.assertThat(activity!!.lifecycleEvents).containsExactly(ON_RESUME, ON_STOP, ON_RESUME).inOrder()
            scenario.moveToState(Lifecycle.State.DESTROYED)
            activity = null
        }
    }

    @Test
    @Ignore("recreate() is calling moveToState(Lifecycle.State.RESUMED) before moveToState(Lifecycle.State.DESTROYED)")
    fun activity_suspend_recreate() {
        var activity: TestActivity? = null
        ActivityScenario.launch<TestActivity>(TestActivity.createIntent(
            listOf(ON_CREATE),
            listOf(ON_RESUME, ON_STOP)
        )).use { scenario ->
            scenario.onActivity { activity = it }
            Truth.assertThat(activity!!.lifecycleEvents).containsExactly(ON_RESUME).inOrder()
            scenario.moveToState(Lifecycle.State.CREATED)
            Truth.assertThat(activity!!.lifecycleEvents).containsExactly(ON_RESUME).inOrder()
            scenario.recreate()
            Truth.assertThat(activity!!.lifecycleEvents).containsExactly(ON_RESUME, ON_STOP, ON_RESUME).inOrder()
            scenario.moveToState(Lifecycle.State.DESTROYED)
            activity = null
        }
    }

    @Test
    fun fragment_suspend_resume() {
        var fragment: TestViewFragment? = null
        val scenario = FragmentScenario.launch(
            TestViewFragment::class.java,
            TestViewFragment.createBundle(
                listOf(ON_CREATE),
                listOf(ON_RESUME, ON_STOP)
            )
        )
        scenario.onFragment { fragment = it }
        Truth.assertThat(fragment!!.lifecycleEvents).containsExactly(ON_RESUME).inOrder()
        scenario.moveToState(Lifecycle.State.CREATED)
        Truth.assertThat(fragment!!.lifecycleEvents).containsExactly(ON_RESUME).inOrder()
        scenario.moveToState(Lifecycle.State.RESUMED)
        Truth.assertThat(fragment!!.lifecycleEvents).containsExactly(ON_RESUME, ON_STOP, ON_RESUME).inOrder()
        scenario.moveToState(Lifecycle.State.DESTROYED)
        fragment = null
    }

    @Test
    @Ignore("recreate() is calling moveToState(Lifecycle.State.RESUMED) before moveToState(Lifecycle.State.DESTROYED)")
    fun fragment_suspend_recreate() {
        var fragment: TestViewFragment? = null
        val scenario = FragmentScenario.launch(
            TestViewFragment::class.java,
            TestViewFragment.createBundle(
                listOf(ON_CREATE),
                listOf(ON_RESUME, ON_STOP)
            )
        )
        scenario.onFragment { fragment = it }
        Truth.assertThat(fragment!!.lifecycleEvents).containsExactly(ON_RESUME).inOrder()
        scenario.moveToState(Lifecycle.State.CREATED)
        Truth.assertThat(fragment!!.lifecycleEvents).containsExactly(ON_RESUME).inOrder()
        scenario.recreate()
        Truth.assertThat(fragment!!.lifecycleEvents).containsExactly(ON_RESUME, ON_STOP, ON_RESUME).inOrder()
        scenario.moveToState(Lifecycle.State.DESTROYED)
        fragment = null
    }

}