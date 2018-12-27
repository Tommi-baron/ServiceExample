package tm.sample.com.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.*
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast


class MessengerService : Service() {

    companion object {
        val MSG_SAY_HELLO = 1
        val ONGOING_NOTIFICATION_ID = "ONGOING"
        val NOTIFICATION_CHANNEL = 99
    }

    val mMessenger = Messenger(IncomingHandler())

    /**
     * Handler of incoming messages from clients.
     */
    internal inner class IncomingHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_SAY_HELLO -> Toast.makeText(applicationContext, "hello!", Toast.LENGTH_SHORT).show()
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onCreate() {
        startForegroundService()

    }

    override fun onBind(intent: Intent?): IBinder? {
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show()
        return mMessenger.getBinder()
    }

    private fun startForegroundService() {

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val drawable = ContextCompat.getDrawable(this, R.drawable.wifi)
        val bitmap = (drawable as BitmapDrawable).bitmap

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(ONGOING_NOTIFICATION_ID, "channel_name", NotificationManager.IMPORTANCE_DEFAULT)

            notificationChannel.description = "channel description"
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder =
            NotificationCompat.Builder(this, ONGOING_NOTIFICATION_ID)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.drawable.ic_stat_name)

                .setContentTitle(getString(R.string.notification_title))
                .setContentIntent(pendingIntent)
                .setContentText(getString(R.string.notification_message))

        startForeground(NOTIFICATION_CHANNEL, notificationBuilder.build())
    }


}