package com.dolev.notification_when_app_is_killed

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import io.flutter.Log
import java.time.Instant

class NotificationOnKillService: Service() {
    private lateinit var title: String
    private lateinit var description: String
    /* 디아콘 추가 시작 */
    private lateinit var channelId: String
    private lateinit var channelName: String
    /* 디아콘 추가 끝 */

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        title = intent?.getStringExtra("title") ?: "Diaconn"
        description = intent?.getStringExtra("description") ?: "Preparing"
        /* 디아콘 추가 시작 */
        channelId = intent?.getStringExtra("channelId") ?: "com.diaconn.app.guard"
        channelName = intent?.getStringExtra("channelName") ?: "DIA:CONN GUARD"
        /* 디아콘 추가 끝 */
        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTaskRemoved(rootIntent: Intent?) {
        try {

            val notificationIntent = packageManager.getLaunchIntentForPackage(packageName)
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

            /* 디아콘 수정 시작 */
            val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification_overlay)
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance)
            /* 디아콘 수정 끝 */

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(Instant.now().epochSecond.toInt(), notificationBuilder.build())
        } catch (e: Exception) {
            Log.d("NotificationOnKillService", "Error showing notification", e)
        }
        super.onTaskRemoved(rootIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
