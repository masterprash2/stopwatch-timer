package life.circles.stopwatch.gateway

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

interface SystemTimeGateway {

    fun getTimeSinceBoot(): Long
    fun getWallClockTime(): Long
    fun isMainThread(): Boolean
    fun completeMeAfter(milliseconds: Long): Observable<Void>
    fun delay(it: Observable<Any>, time: Long, unit: TimeUnit): Observable<*>
    fun registerExpiryAlarmAfter(remainingTime: Long);

}