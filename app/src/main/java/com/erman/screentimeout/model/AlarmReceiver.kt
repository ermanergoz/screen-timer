package com.erman.screentimeout.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        DeviceAdmin(context).lockScreen()
    }
}