package com.erman.screentimer.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.edit
import com.erman.screentimer.util.KEY_ALARM_TIME
import com.erman.screentimer.util.PREFERENCES_NAME
import com.erman.screentimer.util.REQUEST_CODE_ALARM_PENDING_INTENT

class AlarmHandler(private val context: Context) : AlarmScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun setAlarm(triggerAtMillis: Long) {
        cancelAlarm()

        val alarmIntent = getAlarmIntent(0) ?: return

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    alarmIntent
                )
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ->
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, alarmIntent)
            else ->
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, alarmIntent)
        }
        preferences.edit { putLong(KEY_ALARM_TIME, triggerAtMillis) }
    }

    override fun cancelAlarm() {
        getAlarmIntent(PendingIntent.FLAG_NO_CREATE)?.let { alarmIntent ->
            alarmManager.cancel(alarmIntent)
            alarmIntent.cancel()
        }
        preferences.edit { remove(KEY_ALARM_TIME) }
    }

    override fun getAlarmTime(): Long? =
        if (isAlarmSet() && preferences.contains(KEY_ALARM_TIME)) {
            preferences.getLong(KEY_ALARM_TIME, 0)
        } else {
            null
        }

    private fun isAlarmSet(): Boolean = getAlarmIntent(PendingIntent.FLAG_NO_CREATE) != null

    private fun getAlarmIntent(flags: Int): PendingIntent? = PendingIntent.getBroadcast(
        context,
        REQUEST_CODE_ALARM_PENDING_INTENT,
        Intent(context, AlarmReceiver::class.java),
        flags
    )
}
