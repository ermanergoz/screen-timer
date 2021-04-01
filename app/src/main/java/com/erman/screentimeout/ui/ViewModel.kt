package com.erman.screentimeout.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erman.screentimeout.model.AlarmHandler
import com.erman.screentimeout.util.DURATION_DEF_VAL
import com.erman.screentimeout.model.DeviceAdmin
import java.util.*

class ViewModel(
    private val deviceAdmin: DeviceAdmin,
    private val alarmHandler: AlarmHandler
) : ViewModel() {
    private val _activateAdmin = MutableLiveData<Boolean>().apply {
        value = false
    }
    val activateAdmin: LiveData<Boolean> = _activateAdmin

    private val _screenTimeOut = MutableLiveData<Int>().apply {
        value = DURATION_DEF_VAL
    }
    val screenTimeOut: LiveData<Int> = _screenTimeOut

    private val _isAlarmSet = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isAlarmSet: LiveData<Boolean> = _isAlarmSet

    private val _nextAlarm = MutableLiveData<String>().apply {
        value = "-"
    }
    val nextAlarm: LiveData<String> = _nextAlarm

    fun onAdminButtonTapped() {
        if (deviceAdmin.isAdminActive()) {
            deviceAdmin.deactivateAdmin()
        } else {
            _activateAdmin.value = true
        }
    }

    fun onSetButtonTapped() {
        if (alarmHandler.isAlarmSet()) {
            alarmHandler.cancelAlarm()
        } else {
            screenTimeOut.value?.let {
                alarmHandler.setAlarm(it)

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE) + it)
                _nextAlarm.value = calendar.get(Calendar.HOUR_OF_DAY).toString() + " : " +
                        calendar.get(Calendar.MINUTE).toString()
            }
        }
        _isAlarmSet.value = alarmHandler.isAlarmSet()

    }

    fun onDurationChanged(duration: CharSequence, start: Int, before: Int, count: Int) {
        if (duration.isNotEmpty())
            _screenTimeOut.value = duration.toString().toInt()
    }
}