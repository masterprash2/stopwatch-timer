package life.circles.stopwatch.home

import io.reactivex.Observable
import io.reactivex.Scheduler
import life.circles.stopwatch.data.TimerModel
import life.circles.stopwatch.gateway.SystemTimeGateway
import java.util.concurrent.TimeUnit

class TimeRunner constructor(
    private val timerModel: TimerModel,
    private val backgroundThread: Scheduler,
    private val mainThread: Scheduler,
    private val systemTimeGateway: SystemTimeGateway
) {

    fun observeTimeChanges(): Observable<String> {
        return Observable.generate<Long> {
            it.onNext(timerModel.computeRemainingTime())
            it.onComplete()
        }.subscribeOn(mainThread).observeOn(backgroundThread).repeatWhen {
            systemTimeGateway.delay(it, 50, TimeUnit.MILLISECONDS)
        }.takeUntil { remaining ->
            remaining <= 0
        }.map {
            val remaining = it / 1000;
            String.format("%02d:%02d", (remaining / 60), (remaining % 60))
        }.distinctUntilChanged()
            .observeOn(mainThread)
    }

}