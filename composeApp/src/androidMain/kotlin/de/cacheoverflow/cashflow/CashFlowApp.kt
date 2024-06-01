package de.cacheoverflow.cashflow

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CashFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CashFlowApp)
            modules(settingsModule)
        }
    }
}