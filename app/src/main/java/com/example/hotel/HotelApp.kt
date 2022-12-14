package com.example.hotel

import android.app.Application
import com.example.hotel.di.androidModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class HotelApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@HotelApp)
            modules(listOf(androidModule))
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}