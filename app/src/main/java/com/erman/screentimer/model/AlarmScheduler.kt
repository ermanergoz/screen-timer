package com.erman.screentimer.model

interface AlarmScheduler {
    fun setAlarm(triggerAtMillis: Long)
    fun cancelAlarm()
    fun isAlarmSet(): Boolean
}
