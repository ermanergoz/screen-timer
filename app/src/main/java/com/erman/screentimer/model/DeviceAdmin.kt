package com.erman.screentimer.model

interface DeviceAdmin {
    fun isAdminActive(): Boolean
    fun deactivateAdmin()
    fun lockScreen()
}
