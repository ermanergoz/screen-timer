package com.erman.screentimeout

import com.erman.screentimeout.model.AlarmHandler
import com.erman.screentimeout.model.DeviceAdmin
import com.erman.screentimeout.ui.ViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    viewModel { ViewModel(get(), get()) }
    factory { DeviceAdmin(get()) }

    single { AlarmHandler(get()) }
}