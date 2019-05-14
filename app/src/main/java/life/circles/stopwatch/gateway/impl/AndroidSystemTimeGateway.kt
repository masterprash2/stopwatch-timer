package life.circles.stopwatch.gateway.impl

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.subjects.BehaviorSubject
import life.circles.stopwatch.gateway.SystemTimeGateway
import life.circles.stopwatch.receiver.StopwatchReceiver
import java.util.concurrent.TimeUnit


class AndroidSystemTimeGateway(
    private val context: Context
) : SystemTimeGateway {


    private val handler = Handler()

    override fun getTimeSinceBoot(): Long {
        return SystemClock.elapsedRealtime()
    }

    override fun getWallClockTime(): Long {
        return System.currentTimeMillis()
    }

    override fun isMainThread(): Boolean {
        return Looper.getMainLooper() == Looper.myLooper()
    }

    override fun completeMeAfter(milliseconds: Long): Observable<Void> {
        val subject = BehaviorSubject.create<Void>();
        handler.postDelayed(Runnable {
            subject.onComplete()
        }, milliseconds)
        return subject
    }

    override fun delay(it: Observable<Any>, time: Long, unit: TimeUnit): Observable<*> {
        return it.delay(time, unit)
    }

    override fun registerExpiryAlarmAfter(remainingTime: Long) {
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager,
            AlarmManager.ELAPSED_REALTIME,
            remainingTime,
            PendingIntent.getBroadcast(context, 2, Intent(context, StopwatchReceiver::class.java), PendingIntent.FLAG_ONE_SHOT)
        )
    }


}