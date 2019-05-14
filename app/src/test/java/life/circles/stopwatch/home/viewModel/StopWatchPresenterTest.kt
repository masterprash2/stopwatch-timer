package life.circles.stopwatch.home.viewModel

import junit.framework.TestCase.assertFalse
import life.circles.stopwatch.data.TimerModel
import life.circles.stopwatch.gateway.ResourceProvider
import life.circles.stopwatch.home.StopWatchNavigation
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito


class StopWatchPresenterTest {

    lateinit var presenter: StopWatchPresenter
    lateinit var data: StopWatchScreenData
    lateinit var resourceProvider: ResourceProvider
    lateinit var navigation: StopWatchNavigation

    @Before
    fun setup() {
        navigation = Mockito.mock(StopWatchNavigation::class.java)

        resourceProvider = Mockito.mock(ResourceProvider::class.java)
        Mockito.`when`(resourceProvider.getStringButtonStart()).thenReturn("startButton")
        Mockito.`when`(resourceProvider.getStringButtonStartAgain()).thenReturn("startButtonAgain")

        data = StopWatchScreenData(navigation)
        presenter = StopWatchPresenter(data, resourceProvider)
    }

    @Test
    fun stateIdle() {
        presenter.updateState(TimerModel.TimerState.IDLE)
        assertEquals("startButton", data.buttonText.get())
        assertFalse(data.isExpired.get())
        assertEquals("00:00", data.timerString.get())
        assertFalse(data.isTimerRunning.get())
    }

    @Test
    fun stateRunning() {
        presenter.updateState(TimerModel.TimerState.RUNNING)
        assertTrue(data.isTimerRunning.get())
        assertFalse(data.isExpired.get())
    }

    @Test
    fun stateExpired() {
        presenter.updateState(TimerModel.TimerState.EXPIRED)
        assertTrue(data.isExpired.get())
        assertFalse(data.isTimerRunning.get())
        assertEquals("startButtonAgain",data.buttonText.get())
    }

    @Test
    fun multipleStatesIdleToRunningToExpired() {
        stateIdle()
        stateRunning()
        stateExpired()
        stateIdle()
        stateExpired()
    }


    @After
    fun tearDown() {

    }

}