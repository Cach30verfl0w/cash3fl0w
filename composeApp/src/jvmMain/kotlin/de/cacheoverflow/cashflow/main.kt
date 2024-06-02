/*
 * Copyright 2024 Cach30verfl0w
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.cacheoverflow.cashflow

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import de.cacheoverflow.cashflow.ui.components.RootComponent
import de.cacheoverflow.cashflow.utils.DesktopPreferencesProvider
import de.cacheoverflow.cashflow.utils.DesktopSecurityProvider
import de.cacheoverflow.cashflow.utils.IPreferencesProvider
import de.cacheoverflow.cashflow.utils.ISecurityProvider
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

// TODO: Redesign app for both desktop and mobile

val compatibilityModule = module {
    single<ISecurityProvider> { DesktopSecurityProvider() }
    singleOf(::DesktopSecurityProvider)

    single<IPreferencesProvider> { DesktopPreferencesProvider() }
    singleOf(::DesktopPreferencesProvider)
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