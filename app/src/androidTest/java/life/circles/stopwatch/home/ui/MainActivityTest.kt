package life.circles.stopwatch.home.ui

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import life.circles.stopwatch.R
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Before
    fun setUp() {
        Intents.init()
        activityScenario.scenario.onActivity {
            it.viewModel.timerModel.reset()
            it.binding.executePendingBindings()
        }
    }


    @get:Rule
    var activityScenario = activityScenarioRule<MainActivity>()

    @Test
    fun invalidInput() {
        activityScenario.scenario.moveToState(Lifecycle.State.RESUMED)
        onIdle()
        onView(withId(R.id.startButton)).perform(click())
        onIdle()
        onView(withId(R.id.startButtonDialog)).check(matches(isDisplayed()))
        onIdle()
        onView(withId(R.id.inputField)).perform(typeText("0"))
        onIdle()
        onView(withId(R.id.startButtonDialog)).perform(click())
        onIdle()
        onView(withId(R.id.startButtonDialog)).check(matches(isDisplayed()))
        activityScenario.scenario.onActivity {
            it.binding.executePendingBindings()
            assertFalse(it.viewModel.viewData().isTimerRunning.get())
        }
    }


    @Test
    fun startTimer() {
        activityScenario.scenario.moveToState(Lifecycle.State.RESUMED)
        onIdle()
        onView(withId(R.id.startButton)).check(matches(isDisplayed()))
        onView(withId(R.id.startButton)).perform(click())
        onIdle()
        onView(withId(R.id.startButtonDialog)).check(matches(isDisplayed()))
        onIdle()
        onView(withId(R.id.inputField)).perform(typeText("1"))
        onIdle()
        onView(withId(R.id.startButtonDialog)).perform(click())
        onIdle()
        onView(withId(R.id.startButtonDialog)).check(doesNotExist())
        onIdle()
        activityScenario.scenario.onActivity {
            it.binding.executePendingBindings()
            assertTrue(it.viewModel.viewData().isTimerRunning.get())
        }
    }

    @Test
    fun expired() {
        activityScenario.scenario.moveToState(Lifecycle.State.RESUMED)
        onIdle()
        onView(withId(R.id.startButton)).check(matches(isDisplayed()))
        activityScenario.scenario.onActivity {
            it.viewModel.timerModel.startWithMillis(100)
        }
        activityScenario.scenario.onActivity {
            it.binding.executePendingBindings()
            assertTrue(it.viewModel.viewData().isTimerRunning.get())
        }
        Thread.sleep(150)
        activityScenario.scenario.onActivity {
            it.binding.executePendingBindings()
            assertFalse(it.viewModel.viewData().isTimerRunning.get())
            assertTrue(it.viewModel.viewData().isExpired.get())
        }
        onView(withId(R.id.expired)).check(matches(isDisplayed()))

    }


    @After
    fun tearDown() {
        Intents.release()
    }
}