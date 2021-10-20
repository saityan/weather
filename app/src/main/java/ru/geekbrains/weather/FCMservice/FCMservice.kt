package ru.geekbrains.weather.FCMservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.geekbrains.weather.R
import ru.geekbrains.weather.view.MainActivity

class FCMservice : FirebaseMessagingService() {

    companion object {
        private const val PUSH_KEY_TITLE = "title"
        private const val PUSH_KEY_MESSAGE = "message"
        private const val NOTIFICATION_FIRST = 1
        private const val CHANNEL_ID1 = "channel_id1"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val remoteMessageData = remoteMessage.data
        remoteMessageData.let {
            val title = remoteMessageData[PUSH_KEY_TITLE]
            val message = remoteMessageData[PUSH_KEY_MESSAGE]
            if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
                pushNotification(title, message)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    private fun pushNotification(title: String, message: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val intentAction = Intent(this, MainActivity :: class.java)
        intentAction.putExtra("Action", "Some action")
        val intent = PendingIntent.getActivity(
            this, 0,
            intentAction, 0
        )

        val notificationBuilderOne =
            NotificationCompat.Builder(this, CHANNEL_ID1).apply {
                setSmallIcon(R.drawable.ic_map_pin)
                setContentTitle(title)
                setContentText(message)
                addAction(R.drawable.ic_map_marker , "Open application", intent)
                priority = NotificationCompat.PRIORITY_HIGH
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameFirst = "Name $CHANNEL_ID1"
            val descFirst = "Description $CHANNEL_ID1"
            val importanceFirst = NotificationManager.IMPORTANCE_HIGH
            val channelFirst =
                NotificationChannel(CHANNEL_ID1, nameFirst, importanceFirst).apply {
                    description = descFirst
                }
            notificationManager.createNotificationChannel(channelFirst)
        }
        notificationManager.notify(NOTIFICATION_FIRST, notificationBuilderOne.build())
    }
}