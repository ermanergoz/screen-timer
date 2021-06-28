package com.erman.screentimer.app

import android.app.Application
import com.erman.screentimer.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(AppModule)
        }
    }
}
