package com.erman.screentimer.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        AlarmHandler(context).cancelAlarm()
        DeviceAdmin(context).lockScreen()
    }
}