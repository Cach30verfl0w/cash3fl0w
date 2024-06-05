package de.cacheoverflow.cashflow

import android.app.Application
import de.cacheoverflow.cashflow.security.AndroidSecurityProvider
import de.cacheoverflow.cashflow.security.ISecurityProvider
import de.cacheoverflow.cashflow.utils.AndroidPreferencesProvider
import de.cacheoverflow.cashflow.utils.IPreferencesProvider
import de.cacheoverflow.cashflow.utils.settings.PreferencesProvider
import okio.Path
import okio.Path.Companion.toPath
import org.koin.dsl.module


val defaultFileProvider: (Path) -> Path = {
    CashFlowApp.instance?.applicationContext?.filesDir.toString().toPath() / it
}

val compatibilityModule = module {
    single<IPreferencesProvider> { AndroidPreferencesProvider() }
    single<ISecurityProvider> { AndroidSecurityProvider(defaultFileProvider) }
    single<PreferencesProvider> { PreferencesProvider(defaultFileProvider) }
}

class CashFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        var instance: CashFlowApp? = null
    }
}