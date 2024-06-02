package de.cacheoverflow.cashflow

import android.app.Application
import de.cacheoverflow.cashflow.security.AndroidSecurityProvider
import de.cacheoverflow.cashflow.security.ISecurityProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val securityModule = module {
    single<ISecurityProvider> { AndroidSecurityProvider() }
    singleOf(::AndroidSecurityProvider)
}


class CashFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CashFlowApp)
            modules(settingsModule, securityModule)
        }
    }
}