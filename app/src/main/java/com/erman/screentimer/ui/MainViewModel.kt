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

    private val _screenTimeOut = MutableLiveData<Int?>().apply {
        value = DURATION_DEF_VAL
    }
    val screenTimeOut: LiveData<Int?> = _screenTimeOut

    private val _isAlarmSet = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isAlarmSet: LiveData<Boolean> = _isAlarmSet

    private val _nextAlarm = MutableLiveData<String?>()
    val nextAlarm: LiveData<String?> = _nextAlarm

    private val _hideKeyboard = MutableLiveData<Event<Unit>>()
    val hideKeyboard: LiveData<Event<Unit>> = _hideKeyboard

    init {
        if (!deviceAdmin.isAdminActive()) {
            _activateAdmin.value = Event(Unit)
        }
        refreshAlarmState()
    }

    fun onAdminButtonTapped() {
        if (deviceAdmin.isAdminActive()) {
            deviceAdmin.deactivateAdmin()
        } else {
            _activateAdmin.value = Event(Unit)
        }
    }

    fun onSetButtonTapped() {
        if (alarmScheduler.getAlarmTime() != null) {
            alarmScheduler.cancelAlarm()
        } else {
            screenTimeOut.value?.takeIf { it > 0 }?.let {
                alarmScheduler.setAlarm(currentTimeMillis() + TimeUnit.MINUTES.toMillis(it.toLong()))
            }
        }
        refreshAlarmState()
        _hideKeyboard.value = Event(Unit)
    }

    fun onTimeoutChanged(duration: CharSequence) {
        _screenTimeOut.value = duration.toString().toIntOrNull()
    }

    fun refreshAlarmState() {
        val alarmTime = alarmScheduler.getAlarmTime()
        _isAlarmSet.value = alarmTime != null
        _nextAlarm.value = alarmTime?.let { formatTime(it) }
    }

    private fun formatTime(timeInMillis: Long): String {
        val calendar = Calendar.getInstance().apply { this.timeInMillis = timeInMillis }
        return String.format(
            Locale.getDefault(),
            "%02d : %02d",
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )
    }
}
