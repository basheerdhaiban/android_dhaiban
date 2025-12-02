package com.semicolon.dhaiban.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.presentation.main.MainActivity

private const val NOTIFICATION_ID = 1

fun sendOrdersNotification(body: String, title: String, context: Context ,inboxId:Int=Int.MIN_VALUE,typeOfNotification:String) {

//    val contentIntent = Intent(context, AlertDetailsActivity::class.java).apply {
//        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        alert?.let {
//            putExtra(Constants.START_TIME, alert.start)
//            putExtra(Constants.END_TIME, alert.end)
//            putExtra(Constants.DESCRIPTION, alert.description)
//            putExtra(Constants.SENDER_NAME, alert.sender_name)
//            putExtra(Constants.EVENT, alert.event)
//        }
//    }

//    val contentPendingIntent = PendingIntent.getActivity(
//        context,
//        NOTIFICATION_ID,
//        contentIntent,
//        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//    )
Log.d("sendOrdersNotification",inboxId.toString())
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        action = Intent.ACTION_VIEW
        putExtra("destination", typeOfNotification)
        putExtra("inboxID", inboxId)


    }
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//    val pendingIntent: PendingIntent = PendingIntent.getActivity(
//        context,
//        0,
//        intent,
//        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//    )
    val pendingIntent = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(intent)
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
    val builder =
        NotificationCompat.Builder(context, context.getString(R.string.orders_notification_id))
            .apply {
                setSmallIcon(R.drawable.logo)
                setAutoCancel(true)
                setContentTitle(title)
                setContentText(body)
                setContentIntent(pendingIntent)
                setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(body)
                )
                priority = NotificationCompat.PRIORITY_MAX
            }
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.notify(NOTIFICATION_ID, builder.build())
}

fun createOrdersNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            context.getString(R.string.orders_notification_id),
            context.getString(R.string.orders_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            description = context.getString(R.string.orders_channel_description)
        }
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}