package com.erman.screentimer.model

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context

class DevicePolicyAdmin(context: Context) : DeviceAdmin {
    private val devicePolicyManager =
        context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    private val adminComponent = ComponentName(context, AdminReceiver::class.java)

    override fun isAdminActive(): Boolean = devicePolicyManager.isAdminActive(adminComponent)

    override fun deactivateAdmin() {
        devicePolicyManager.removeActiveAdmin(adminComponent)
    }

    override fun lockScreen() {
        if (isAdminActive()) {
            devicePolicyManager.lockNow()
        }
    }
}
