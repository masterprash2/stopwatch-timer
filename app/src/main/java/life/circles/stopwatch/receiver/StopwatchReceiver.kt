package life.circles.stopwatch.receiver

import android.content.Context
import android.content.Intent
import dagger.android.DaggerBroadcastReceiver
import life.circles.stopwatch.data.TimerModel
import javax.inject.Inject


class StopwatchReceiver : DaggerBroadcastReceiver() {

    @Inject
    lateinit var timerModel: TimerModel

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent != null && intent.action.equals("", true)) {
            if (intent.action == "expiryAlarm" ||
                intent.action == Intent.ACTION_LOCKED_BOOT_COMPLETED ||
                intent.action == Intent.ACTION_BOOT_COMPLETED
                || intent.action == Intent.ACTION_TIME_CHANGED
            ) {
                timerModel.syncWithTimeChange()
            }
        }
    }
}