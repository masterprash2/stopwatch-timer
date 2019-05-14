package life.circles.stopwatch.home.viewModel

import io.reactivex.schedulers.Schedulers
import life.circles.stopwatch.data.TimerModel
import life.circles.stopwatch.gateway.NotificationService
import life.circles.stopwatch.gateway.ResourceProvider
import life.circles.stopwatch.home.StopWatchNavigation
import life.circles.stopwatch.home.TimeRunner
import life.circles.stopwatch.mock.MemoryPreferenceStore
import life.circles.stopwatch.mock.SystemTimeGatewayMock
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

class StopWatchViewModelTest {

    lateinit var stopwatchViewModel: StopWatchViewModel
    lateinit var presenter: StopWatchPresenter
    lateinit var data: StopWatchScreenData
    lateinit var timerModel: TimerModel
    lateinit var resourceProvider: ResourceProvider
    lateinit var navigation: StopWatchNavigation
    lateinit var timeGateway: SystemTimeGatewayMock
    lateinit var preferenceStore: MemoryPreferenceStore
    lateinit var timeRunner: TimeRunner
    lateinit var notificationService: NotificationService

    @Before
    fun setUp() {
        notificationService = Mockito.mock(NotificationService::class.java)
        timeGateway = SystemTimeGatewayMock()
        preferenceStore = MemoryPreferenceStore()
        timerModel = TimerModel(timeGateway, preferenceStore, notificationService)
        timeRunner = TimeRunner(timerModel, Schedulers.trampoline(), Schedulers.trampoline(), timeGateway)

        resourceProvider = Mockito.mock(ResourceProvider::class.java)
        Mockito.`when`(resourceProvider.getStringButtonStart()).thenReturn("startButton")
        Mockito.`when`(resourceProvider.getStringButtonStartAgain()).thenReturn("startButtonAgain")

        navigation = Mockito.mock(StopWatchNavigation::class.java)
        data = StopWatchScreenData(navigation)
        presenter = StopWatchPresenter(data, resourceProvider)
        stopwatchViewModel = StopWatchViewModel(presenter, timerModel, timeRunner)
    }

    @Test
    fun freshSetup() {
        stopwatchViewModel.setup()
        assertEquals("startButton", data.buttonText.get())
        assertFalse(data.isExpired.get())
        assertFalse(data.isTimerRunning.get())
    }

    @Test
    fun startTimer() {
        stopwatchViewModel.setup()
        assertEquals("00:00", data.timerString.get())
        assertFalse(data.isTimerRunning.get())
        assertEquals(TimerModel.TimerState.IDLE, data.timerState)
        stopwatchViewModel.beginStopWatch()
        verify(navigation).showPrompt()
        assertEquals(TimerModel.TimerState.IDLE, data.timerState)
        timerModel.start(1)
        assertEquals(TimerModel.TimerState.RUNNING, data.timerState)
        assertTrue(data.isTimerRunning.get())
        assertEquals("01:00", data.timerString.get())
    }

    @Test
    fun resetTimer() {
        timerModel.start(1)
        timeGateway.advanceTimeBy(61000)
        stopwatchViewModel.setup()
        assertEquals("00:00", data.timerString.get())
        assertFalse(data.isTimerRunning.get())
        assertEquals(TimerModel.TimerState.EXPIRED, data.timerState)
        stopwatchViewModel.beginStopWatch()
        verifyNoMoreInteractions(navigation)
        assertEquals(TimerModel.TimerState.IDLE, data.timerState)
        assertFalse(data.isTimerRunning.get())
    }


    @After
    fun tearDown() {
    }
}