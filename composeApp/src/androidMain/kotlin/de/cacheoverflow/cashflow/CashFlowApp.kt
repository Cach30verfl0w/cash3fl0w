package de.cacheoverflow.cashflow

import android.app.Application
import de.cacheoverflow.cashflow.utils.AndroidPreferencesProvider
import de.cacheoverflow.cashflow.utils.security.AndroidSecurityProvider
import de.cacheoverflow.cashflow.utils.IPreferencesProvider
import de.cacheoverflow.cashflow.utils.security.AbstractSecurityProvider
import de.cacheoverflow.cashflow.utils.settings.PreferencesProvider
import okio.Path.Companion.toPath
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.io.File

val compatibilityModule = module {
    single<AbstractSecurityProvider> { AndroidSecurityProvider() }
    single<IPreferencesProvider> { AndroidPreferencesProvider() }
    single<PreferencesProvider> { PreferencesProvider({
        (File(CashFlowApp.instance?.applicationContext?.filesDir, it)).toString().toPath()
    }) }
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