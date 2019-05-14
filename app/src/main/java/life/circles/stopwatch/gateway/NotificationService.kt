package life.circles.stopwatch.gateway

interface NotificationService {

    fun showExpiredNotification()
    fun removeAll()

}