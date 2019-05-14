package life.circles.stopwatch.home

import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import life.circles.stopwatch.data.TimerModel
import life.circles.stopwatch.gateway.NotificationService
import life.circles.stopwatch.mock.MemoryPreferenceStore
import life.circles.stopwatch.mock.SystemTimeGatewayMock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class TimeRunnerTest {

    lateinit var timeRunner: TimeRunner
    lateinit var timerModel: TimerModel
    lateinit var memoryPreferenceStore: MemoryPreferenceStore
    lateinit var systemTimeGatewayMock: SystemTimeGatewayMock
    lateinit var notificationService: NotificationService

    @Before
    fun setUp() {

        memoryPreferenceStore = MemoryPreferenceStore()
        systemTimeGatewayMock = SystemTimeGatewayMock()
        notificationService = Mockito.mock(NotificationService::class.java)
        timerModel = TimerModel(systemTimeGatewayMock, memoryPreferenceStore, notificationService)
        timeRunner = TimeRunner(timerModel, Schedulers.trampoline(), Schedulers.trampoline(), systemTimeGatewayMock)
    }

    @Test
    fun observeTimerZeroRemainingTime() {
        val observer = TestObserver<String>()
        timeRunner.observeTimeChanges().subscribe(observer)
        observer.assertValueAt(0, "00:00")
    }

    @Test
    fun observeTimerWithRemainingTime() {
        val observer = TestObserver<String>()
        timerModel.start(1)
        timeRunner.observeTimeChanges().subscribe(observer)
        observer.assertValueAt(0, "01:00")
        systemTimeGatewayMock.advanceTimeBy(5000)
        observer.assertValueAt(1, "00:55")
        systemTimeGatewayMock.advanceTimeBy(5000)
        observer.assertValueAt(2, "00:50")
        systemTimeGatewayMock.advanceTimeBy(50000)
        observer.assertValueAt(3, "00:00")
        systemTimeGatewayMock.advanceTimeBy(50000)
        systemTimeGatewayMock.advanceTimeBy(50000)
        systemTimeGatewayMock.advanceTimeBy(50000)
        observer.assertValueCount(4)
    }

    @After
    fun tearDown() {

    }
}