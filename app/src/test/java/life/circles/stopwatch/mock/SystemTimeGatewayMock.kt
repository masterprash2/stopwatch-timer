package life.circles.stopwatch.mock

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import life.circles.stopwatch.gateway.SystemTimeGateway
import java.util.concurrent.TimeUnit

class SystemTimeGatewayMock : SystemTimeGateway {


    private var timeSinceBoot = 0L
    private var wallClockTime = 0L
    private var isMainThread: Boolean = true
    private var advanceTimePublisher = BehaviorSubject.createDefault(0L)
    private var totalTimeAdvanced = 0L

    private var publisherSubject = PublishSubject.create<Any>()

    override fun getTimeSinceBoot(): Long {
        return timeSinceBoot
    }

    fun advanceTimeBy(milliseconds: Long) {
        timeSinceBoot += milliseconds
        wallClockTime += milliseconds
        totalTimeAdvanced += milliseconds
        advanceTimePublisher.onNext(totalTimeAdvanced)
        publisherSubject.onNext(1)
    }

    override fun getWallClockTime(): Long {
        return wallClockTime
    }

    fun setWallClock(time: Long) {
        wallClockTime = time
    }

    fun resetBootTime() {
        timeSinceBoot = 0
    }

    fun setMainThread(value: Boolean) {
        isMainThread = value;
    }

    override fun isMainThread(): Boolean {
        return isMainThread
    }

    override fun completeMeAfter(milliseconds: Long): Observable<Void> {
        val expiry = milliseconds + totalTimeAdvanced
        return advanceTimePublisher
            .filter { it >= expiry }
            .take(1).flatMap { Observable.empty<Void>() }
    }

    override fun delay(it: Observable<Any>, time: Long, unit: TimeUnit): Observable<*> {
        return it.flatMap { publisherSubject }
    }

    override fun registerExpiryAlarmAfter(remainingTime: Long) {

    }



}