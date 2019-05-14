package life.circles.stopwatch.data

import io.reactivex.observers.TestObserver
import life.circles.stopwatch.gateway.NotificationService
import life.circles.stopwatch.mock.MemoryPreferenceStore
import life.circles.stopwatch.mock.SystemTimeGatewayMock
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify

class TimerModelTest {

    lateinit var timerModel: TimerModel
    lateinit var platformGateway: SystemTimeGatewayMock
    val memoryPreferenceStore = MemoryPreferenceStore()
    lateinit var notificationService: NotificationService

    @Before
    fun setUp() {
        platformGateway = SystemTimeGatewayMock()
        notificationService = Mockito.mock(NotificationService::class.java)
        timerModel = TimerModel(platformGateway, memoryPreferenceStore, notificationService)
    }

    @Test
    fun startTimer() {
        assertEquals(TimerModel.TimerState.IDLE, timerModel.getTimerState())
        timerModel.start(1)
        assertEquals(60000, timerModel.computeRemainingTime())
        assertEquals(TimerModel.TimerState.RUNNING, timerModel.getTimerState())
    }


    @Test
    fun testTimeElapsed() {
        startTimer()
        val remainingTime = timerModel.computeRemainingTime()
        platformGateway.advanceTimeBy(500)
        assertEquals(remainingTime - 500, timerModel.computeRemainingTime())
    }

    @Test
    fun testTimeExpired() {
        startTimer()
        platformGateway.advanceTimeBy(61000)
        assertEquals(0, timerModel.computeRemainingTime())
        assertEquals(TimerModel.TimerState.EXPIRED, timerModel.getTimerState())
    }

    @Test
    fun resume() {
        startTimer()
        platformGateway.advanceTimeBy(500)
        timerModel = TimerModel(platformGateway, memoryPreferenceStore, notificationService)
        assertEquals(TimerModel.TimerState.RUNNING, timerModel.getTimerState())
        assertEquals(59500, timerModel.computeRemainingTime())
    }

    @Test
    fun resumeAfterDelay() {
        startTimer()
        platformGateway.advanceTimeBy(500)
        timerModel = TimerModel(platformGateway, memoryPreferenceStore, notificationService)
        platformGateway.advanceTimeBy(500)
        assertEquals(TimerModel.TimerState.RUNNING, timerModel.getTimerState())
        assertEquals(59000, timerModel.computeRemainingTime())
    }


    @Test
    fun resumeAfterReboot() {
        platformGateway.setWallClock(5000)
        startTimer()
        platformGateway.advanceTimeBy(500)
        platformGateway.resetBootTime()
        timerModel = TimerModel(platformGateway, memoryPreferenceStore, notificationService)
        platformGateway.advanceTimeBy(500)
        assertEquals(59000, timerModel.computeRemainingTime())
    }

    @Test
    fun timeChange() {
        platformGateway.setWallClock(5000)
        startTimer()
        platformGateway.advanceTimeBy(500)
        platformGateway.setWallClock(8000)
        timerModel.syncWithTimeChange()
        platformGateway.resetBootTime()
        timerModel = TimerModel(platformGateway, memoryPreferenceStore, notificationService)
        platformGateway.advanceTimeBy(500)
        assertEquals(59000, timerModel.computeRemainingTime())
    }


    @Test(expected = IllegalAccessError::class)
    fun backgroundThreadExecutionStart() {
        platformGateway.setMainThread(false)
        timerModel.start(5)
    }

    @Test(expected = IllegalAccessError::class)
    fun backgroundThreadExecutionSyncWithTimeChange() {
        platformGateway.setMainThread(false)
        timerModel.syncWithTimeChange()
    }

    @Test(expected = IllegalAccessError::class)
    fun backgroundThreadExecutionCompute() {
        platformGateway.setMainThread(false)
        timerModel.computeRemainingTime()
        timerModel.getTimerState()
    }

    @Test(expected = IllegalAccessError::class)
    fun backgroundThreadExecutionTimerState() {
        platformGateway.setMainThread(false)
        timerModel.getTimerState()
    }

    @Test
    fun timeExpiredNotification() {
        val observer = TestObserver<TimerModel.TimerState>()
        timerModel.observeTimerState().subscribe(observer)
        assertEquals(1, observer.valueCount())
        observer.assertValueAt(0, TimerModel.TimerState.IDLE)

        startTimer()
        assertEquals(2, observer.valueCount())
        observer.assertValueAt(1, TimerModel.TimerState.RUNNING)
        observer.assertNoErrors()

        platformGateway.advanceTimeBy(61000)
        assertEquals(3, observer.valueCount())
        observer.assertValueAt(2, TimerModel.TimerState.EXPIRED)
        verify(notificationService).showExpiredNotification()
    }

    @Test
    fun timerReset() {
        startTimer()
        platformGateway.advanceTimeBy(60001)
        assertEquals(TimerModel.TimerState.EXPIRED, timerModel.observeTimerState().blockingFirst())
        timerModel.reset()
        val timerModel = TimerModel(platformGateway, memoryPreferenceStore, notificationService)
        assertEquals(TimerModel.TimerState.IDLE, timerModel.observeTimerState().blockingFirst())
    }


    @After
    fun tearDown() {
    }
}