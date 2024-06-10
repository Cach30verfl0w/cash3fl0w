/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import de.cacheoverflow.cashflow.security.ISecurityProvider
import de.cacheoverflow.cashflow.ui.components.RootComponent
import de.cacheoverflow.cashflow.utils.DesktopSecurityProvider
import de.cacheoverflow.cashflow.utils.settings.PreferencesProvider
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.nio.file.Paths

val defaultFileProvider: (Path) -> Path = {
    Paths.get(System.getProperty("user.home"), ".cash3fl0w").toString().toPath() / it
}

val compatibilityModule = module {
    single<ISecurityProvider> { DesktopSecurityProvider() }
    single<PreferencesProvider> { PreferencesProvider(defaultFileProvider) }
}

fun main() = application {
    startKoin {
        modules(compatibilityModule, sharedModule)
    }

    val root = remember { RootComponent(DefaultComponentContext(LifecycleRegistry())) }
    Window(onCloseRequest = ::exitApplication, title = "Cash3Fl0w") {
        App(root)
    }
}