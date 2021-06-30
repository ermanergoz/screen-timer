package com.erman.screentimer

import com.erman.screentimer.model.AlarmHandler
import com.erman.screentimer.model.DeviceAdmin
import com.erman.screentimer.ui.ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    viewModel { ViewModel(get(), get()) }
    factory { DeviceAdmin(get()) }

    single { AlarmHandler(get()) }
}