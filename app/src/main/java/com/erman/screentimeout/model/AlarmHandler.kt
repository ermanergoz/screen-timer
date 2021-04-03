package com.erman.screentimeout.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.erman.screentimeout.util.REQUEST_CODE_ALARM_PENDING_INTENT
import java.util.*

class AlarmHandler(private val context: Context) {
    private fun setAlarm(calendar: Calendar, intent: PendingIntent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    intent
                )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ->
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent)
            else ->
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, intent)
        }
    }

    fun setAlarm(screenTimeOut: Int) {
        cancelAlarm()

        val screenLockAlarm = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, REQUEST_CODE_ALARM_PENDING_INTENT, intent, 0)
        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE) + screenTimeOut)
            set(Calendar.SECOND, Calendar.getInstance().get(Calendar.SECOND))
        }

        setAlarm(calendar, screenLockAlarm)
    }

    fun cancelAlarm() {
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            REQUEST_CODE_ALARM_PENDING_INTENT,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(alarmIntent)
        alarmIntent.cancel()
    }

    fun isAlarmSet(): Boolean {
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_ALARM_PENDING_INTENT,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }
}