package com.glooko.freefall

import android.app.Application
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.glooko.freefall.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class FreeFallApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initXLog()
        startKoin() {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@FreeFallApp)
            modules(localModule, dataModule, domainModule, sensorModule, presentationModule)
        }
    }

    private fun initXLog() {
        XLog.init(
            LogConfiguration.Builder()
                .logLevel(
                    if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE
                )
                .tag("FF-APP")
                .build()
        )
    }
}