package life.circles.stopwatch.formDialog.interactor

import life.circles.stopwatch.data.TimerModel
import life.circles.stopwatch.gateway.NotificationService
import life.circles.stopwatch.mock.MemoryPreferenceStore
import life.circles.stopwatch.mock.SystemTimeGatewayMock
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito

class StartTimerInteractorTest {

    lateinit var startTimerInteractor: StartTimerInteractor
    lateinit var timerModel: TimerModel
    lateinit var notificationService: NotificationService
    lateinit var timeGateway: SystemTimeGatewayMock
    lateinit var preferenceStore: MemoryPreferenceStore

    @Before
    fun setUp() {
        notificationService = Mockito.mock(NotificationService::class.java)
        timeGateway = SystemTimeGatewayMock()
        preferenceStore = MemoryPreferenceStore()
        timerModel = TimerModel(timeGateway, preferenceStore, notificationService)
        startTimerInteractor = StartTimerInteractor(timerModel)
    }

    @Test
    fun testValidResponse() {
        assertTrue(startTimerInteractor.startTimer("12").first)
        assertTrue(startTimerInteractor.startTimer("1").first)
        assertTrue(startTimerInteractor.startTimer("60").first)
    }

    @Test
    fun invalidResponse() {
        assertFalse(startTimerInteractor.startTimer("0").first)
        assertFalse(startTimerInteractor.startTimer("-1").first)
        assertFalse(startTimerInteractor.startTimer("61").first)
        assertFalse(startTimerInteractor.startTimer("abc").first)
        assertFalse(startTimerInteractor.startTimer(".").first)
        assertFalse(startTimerInteractor.startTimer(".1").first)
        assertFalse(startTimerInteractor.startTimer("1.1").first)
        assertFalse(startTimerInteractor.startTimer("60a").first)
    }

    @After
    fun tearDown() {
    }
}