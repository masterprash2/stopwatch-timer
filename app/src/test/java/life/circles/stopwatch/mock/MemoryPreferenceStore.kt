package life.circles.stopwatch.mock

import life.circles.stopwatch.data.Timer
import life.circles.stopwatch.gateway.PreferenceStore

class MemoryPreferenceStore :PreferenceStore {

    private var timer: Timer? = null
    private var isNotificationShown: Boolean = false

    override fun readTimer(): Timer? {
        return timer
    }

    override fun saveTimer(timer: Timer?) {
        this.timer = timer
    }

    override fun isNotificationShown(): Boolean {
        return isNotificationShown
    }

    override fun setNotificationShown(value: Boolean) {
        this.isNotificationShown = value
    }




}