package life.circles.stopwatch.gateway

import life.circles.stopwatch.data.Timer

interface PreferenceStore {

    fun readTimer(): Timer?
    fun saveTimer(timer: Timer?)
    fun isNotificationShown(): Boolean
    fun setNotificationShown(boolean: Boolean)


}