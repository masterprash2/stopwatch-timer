package life.circles.stopwatch.gateway.impl

import android.content.SharedPreferences
import life.circles.stopwatch.data.Timer
import life.circles.stopwatch.gateway.PreferenceStore
import javax.inject.Inject

class AndroidPreferenceStore @Inject constructor(private val appPrefrence: SharedPreferences) : PreferenceStore {

    private val KEY_TIME_REMAINING = "timeRemaining"
    private val KEY_LAST_BOOT_TIME_AT_TIMER_START = "bootTime"
    private val KEY_LAST_CLOCK_TIME_AT_TIMER_START = "clockTime"
    private val KEY_NOTIFICATION_SHOWN = "notificationShown"

    override fun readTimer(): Timer? {
        if (appPrefrence.contains(KEY_TIME_REMAINING)) {
            val timeRemaining = appPrefrence.getLong(KEY_TIME_REMAINING, 0)
            val bootTime = appPrefrence.getLong(KEY_LAST_BOOT_TIME_AT_TIMER_START, 0)
            val clockTime = appPrefrence.getLong(KEY_LAST_CLOCK_TIME_AT_TIMER_START, 0)
            return Timer(timeRemaining, bootTime, clockTime)
        } else {
            return null;
        }
    }

    override fun saveTimer(timer: Timer?) {
        if (timer != null) {
            appPrefrence.edit()
                .putLong(KEY_TIME_REMAINING, timer.remainingTime)
                .putLong(KEY_LAST_BOOT_TIME_AT_TIMER_START, timer.lastStartTimeSinceBoot)
                .putLong(KEY_LAST_CLOCK_TIME_AT_TIMER_START, timer.lastClockTime).apply()
        } else {
            appPrefrence.edit()
                .remove(KEY_TIME_REMAINING)
                .remove(KEY_LAST_BOOT_TIME_AT_TIMER_START)
                .remove(KEY_LAST_CLOCK_TIME_AT_TIMER_START).apply()
        }
    }

    override fun isNotificationShown(): Boolean {
        return appPrefrence.getBoolean(KEY_NOTIFICATION_SHOWN, false)
    }

    override fun setNotificationShown(boolean: Boolean) {
        appPrefrence.edit().putBoolean(KEY_NOTIFICATION_SHOWN, boolean).apply()
    }


}
