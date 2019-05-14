package life.circles.stopwatch.gateway.impl

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import life.circles.stopwatch.R
import life.circles.stopwatch.gateway.NotificationService
import javax.inject.Inject

class NotificationServiceImpl @Inject constructor(private val context: Context) : NotificationService {

    init {
        createNotificationChannel()
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channelname"
            val descriptionText = "channeldesc"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun showExpiredNotification() {
        val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setContentTitle("Time has expired")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.notification_icon_background)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(1,builder)
    }

    override fun removeAll() {
        NotificationManagerCompat.from(context).cancelAll()
    }

}