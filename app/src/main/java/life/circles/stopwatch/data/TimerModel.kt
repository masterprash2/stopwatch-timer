package life.circles.stopwatch.data

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import life.circles.stopwatch.application.di.AppScope
import life.circles.stopwatch.gateway.NotificationService
import life.circles.stopwatch.gateway.PreferenceStore
import life.circles.stopwatch.gateway.SystemTimeGateway
import javax.inject.Inject


@AppScope
class TimerModel @Inject constructor(
    private val systemTime: SystemTimeGateway,
    private val preferenceStore: PreferenceStore,
    private val notificationService: NotificationService
) {

    enum class TimerState {
        IDLE,
        RUNNING,
        EXPIRED;
    }

    private var alarmDisposable: Disposable? = null
    private var timer: Timer? = null;
    private val statePublisher = BehaviorSubject.createDefault(TimerState.IDLE)

    init {
        resume()
    }

    private fun resume() {
        syncAfterReboot()
    }

    private fun syncAfterReboot() {
        val timer = preferenceStore.readTimer();
        if (timer != null) {
            val timeSinceBoot = systemTime.getTimeSinceBoot()
            val clockTime = systemTime.getWallClockTime()
            val diff = Math.max(0, clockTime - timer.lastClockTime)
            val remainingTime = timer.remainingTime - diff
            updateTimer(remainingTime, timeSinceBoot, clockTime)
        }
    }

    fun start(minutes: Long) {
        startWithMillis(minutes * 60000)
    }

    fun startWithMillis(millis : Long) {
        enforceMainThread()
        val remainingTime = millis
        val lastStartTimeSinceBoot = systemTime.getTimeSinceBoot()
        val lastClockTime = systemTime.getWallClockTime()
        updateTimer(remainingTime, lastStartTimeSinceBoot, lastClockTime)
    }

    private fun updateTimer(
        remainingTime: Long,
        timeSinceBoot: Long,
        clockTime: Long
    ) {
        this.timer = Timer(remainingTime, timeSinceBoot, clockTime)
        preferenceStore.saveTimer(timer!!)
        expireOrWait()
        systemTime.registerExpiryAlarmAfter(remainingTime)
    }

    private fun expireOrWait() {
        if (alarmDisposable != null) {
            alarmDisposable?.dispose()
        }
        val remainingTime = computeRemainingTime();
        if (remainingTime == 0L) {
            statePublisher.onNext(TimerState.EXPIRED)
            if (!preferenceStore.isNotificationShown()) {
                notificationService.showExpiredNotification()
                preferenceStore.setNotificationShown(true)
            }
        } else {
            statePublisher.onNext(TimerState.RUNNING)
            this.alarmDisposable = systemTime.completeMeAfter(remainingTime).doFinally { expireOrWait() }.subscribe()
        }
    }

    fun checkExpired() {
        if(timer != null) {
            expireOrWait()
        }
    }


    fun computeRemainingTime(): Long {
        enforceMainThread()
        if (timer != null) {
            val remaining =
                Math.max(0, timer!!.remainingTime - (systemTime.getTimeSinceBoot() - timer!!.lastStartTimeSinceBoot))

            return remaining
        } else {
            return 0
        }
    }

    fun getTimerState(): TimerState {
        enforceMainThread()
        if (timer != null) {
            if (computeRemainingTime() == 0L) {
                return TimerState.EXPIRED
            } else {
                return TimerState.RUNNING
            }
        }
        return TimerState.IDLE
    }

    fun syncWithTimeChange() {
        enforceMainThread()
        var mTimer = timer;
        if (mTimer != null) {
            val timeSinceBoot = systemTime.getTimeSinceBoot()
            val clockTime = systemTime.getWallClockTime()
            val remaining = mTimer.remainingTime - (systemTime.getTimeSinceBoot() - mTimer.lastStartTimeSinceBoot)
            updateTimer(remaining, timeSinceBoot, clockTime)
        }
    }


    private fun enforceMainThread() {
        if (!systemTime.isMainThread()) {
            throw IllegalAccessError("Can only be accessed by main thread")
        }
    }

    fun observeTimerState(): Observable<TimerState> {
        return statePublisher.distinctUntilChanged()
    }

    fun reset() {
        preferenceStore.saveTimer(null)
        preferenceStore.setNotificationShown(false)
        statePublisher.onNext(TimerState.IDLE)
        notificationService.removeAll();
    }


}