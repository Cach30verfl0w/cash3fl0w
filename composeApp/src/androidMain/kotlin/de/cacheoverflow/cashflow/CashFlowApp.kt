package de.cacheoverflow.cashflow

import android.app.Application
import de.cacheoverflow.cashflow.store.CashFlowSettingsHolder
import de.cacheoverflow.cashflow.store.DefaultCashFlowSettingsHolder
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val module = module {
    single<CashFlowSettingsHolder> { DefaultCashFlowSettingsHolder() }
    singleOf(::DefaultCashFlowSettingsHolder)
}

class CashFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CashFlowApp)
            modules(module)
        }
    }
}