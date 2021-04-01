package com.erman.screentimeout.model

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class DeviceAdmin(private val context: Context) {
    fun isAdminActive(): Boolean {
        val compName = ComponentName(context, AdminReceiver::class.java)
        val devicePolicyManager =
            context.getSystemService(AppCompatActivity.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        return devicePolicyManager.isAdminActive(compName)
    }

    fun deactivateAdmin() {
        val componentName = ComponentName(context, AdminReceiver::class.java)
        val devicePolicyManager =
            context.getSystemService(AppCompatActivity.DEVICE_POLICY_SERVICE) as DevicePolicyManager

        devicePolicyManager.removeActiveAdmin(componentName)
    }

    fun lockScreen() {
        val devicePolicyManager =
            context.getSystemService(AppCompatActivity.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(context, AdminReceiver::class.java)

        val active = devicePolicyManager.isAdminActive(componentName)
        if (active) {
            devicePolicyManager.lockNow()
        }
    }
}