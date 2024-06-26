/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow

import android.app.Application
import de.cacheoverflow.cashflow.security.AndroidSecurityProvider
import de.cacheoverflow.cashflow.security.ISecurityProvider
import de.cacheoverflow.cashflow.utils.settings.PreferencesProvider
import okio.Path
import okio.Path.Companion.toPath
import org.koin.dsl.module

val defaultFileProvider: (Path) -> Path = {
    CashFlowApp.instance?.applicationContext?.filesDir.toString().toPath() / it
}

val compatibilityModule = module {
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