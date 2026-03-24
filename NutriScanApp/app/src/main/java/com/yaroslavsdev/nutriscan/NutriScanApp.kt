package com.yaroslavsdev.nutriscan

import android.app.Application
import com.yaroslavsdev.nutriscan.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class NutriScanApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NutriScanApp)
            modules(appModule)
        }
    }
}