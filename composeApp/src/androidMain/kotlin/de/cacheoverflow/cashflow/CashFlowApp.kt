package de.cacheoverflow.cashflow

import android.app.Application
import de.cacheoverflow.cashflow.utils.AndroidPreferencesProvider
import de.cacheoverflow.cashflow.utils.AndroidSecurityProvider
import de.cacheoverflow.cashflow.utils.IPreferencesProvider
import de.cacheoverflow.cashflow.utils.ISecurityProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val compatibilityModule = module {
    single<ISecurityProvider> { AndroidSecurityProvider() }
    singleOf(::AndroidSecurityProvider)

    single<IPreferencesProvider> { AndroidPreferencesProvider() }
    singleOf(::AndroidPreferencesProvider)
}


class CashFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(this@CashFlowApp)
            modules(sharedModule, compatibilityModule)
        }
    }

    companion object {
        var instance: CashFlowApp? = null
    }
}