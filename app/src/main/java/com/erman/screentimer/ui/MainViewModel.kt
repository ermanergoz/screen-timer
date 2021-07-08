package com.erman.screentimer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erman.screentimer.model.AlarmScheduler
import com.erman.screentimer.model.DeviceAdmin
import com.erman.screentimer.util.DURATION_DEF_VAL
import com.erman.screentimer.util.Event
import java.util.*
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val deviceAdmin: DeviceAdmin,
    private val alarmScheduler: AlarmScheduler,
    private val currentTimeMillis: () -> Long = System::currentTimeMillis
) : ViewModel() {
    private val _activateAdmin = MutableLiveData<Event<Unit>>()
    val activateAdmin: LiveData<Event<Unit>> = _activateAdmin

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

    private val _hideKeyboard = MutableLiveData<Event<Unit>>()
    val hideKeyboard: LiveData<Event<Unit>> = _hideKeyboard

    init {
        if (!deviceAdmin.isAdminActive()) {
            _activateAdmin.value = Event(Unit)
        }
    }

    fun onAdminButtonTapped() {
        if (deviceAdmin.isAdminActive()) {
            deviceAdmin.deactivateAdmin()
        } else {
            _activateAdmin.value = Event(Unit)
        }
    }

    fun onSetButtonTapped() {
        if (alarmScheduler.isAlarmSet()) {
            alarmScheduler.cancelAlarm()
        } else {
            screenTimeOut.value?.let {
                val triggerAtMillis = currentTimeMillis() + TimeUnit.MINUTES.toMillis(it.toLong())
                alarmScheduler.setAlarm(triggerAtMillis)
                _nextAlarm.value = formatTime(triggerAtMillis)
            }
        }
        _isAlarmSet.value = alarmScheduler.isAlarmSet()
        _hideKeyboard.value = Event(Unit)
    }

    fun onTimeoutChanged(duration: CharSequence, start: Int, before: Int, count: Int) {
        if (duration.isNotEmpty())
            _screenTimeOut.value = duration.toString().toInt()
    }

    fun refreshAlarmState() {
        _isAlarmSet.value = alarmScheduler.isAlarmSet()
    }

    private fun formatTime(timeInMillis: Long): String {
        val calendar = Calendar.getInstance().apply { this.timeInMillis = timeInMillis }
        return calendar.get(Calendar.HOUR_OF_DAY).toString() + " : " +
                calendar.get(Calendar.MINUTE).toString()
    }
}
