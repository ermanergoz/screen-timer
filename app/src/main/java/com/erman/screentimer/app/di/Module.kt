package com.erman.screentimer.app.di

import com.erman.screentimer.model.AlarmHandler
import com.erman.screentimer.model.AlarmScheduler
import com.erman.screentimer.model.DeviceAdmin
import com.erman.screentimer.model.DevicePolicyAdmin
import com.erman.screentimer.ui.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get(), get()) }
    single<DeviceAdmin> { DevicePolicyAdmin(androidContext()) }
    single<AlarmScheduler> { AlarmHandler(androidContext()) }
}
