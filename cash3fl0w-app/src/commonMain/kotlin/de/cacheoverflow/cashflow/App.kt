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

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import de.cacheoverflow.cashflow.ui.DefaultColorScheme
import de.cacheoverflow.cashflow.ui.HomeScreen
import de.cacheoverflow.cashflow.ui.settings.Settings
import de.cacheoverflow.cashflow.ui.components.OptionalAuthLock
import de.cacheoverflow.cashflow.ui.components.RootComponent
import de.cacheoverflow.cashflow.ui.settings.AuthSettings
import de.cacheoverflow.cashflow.utils.unlockAccountInfo
import de.cacheoverflow.cashflow.utils.unlockAccountInfoSubtitle
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.dsl.module

interface IErrorHandler {
    val error: MutableStateFlow<Exception?>
}

val sharedModule = module {
    single<IErrorHandler> {
        object: IErrorHandler {
            override val error: MutableStateFlow<Exception?> = MutableStateFlow(null)
        }
    }
}

@Composable
fun AppView(
    childStack: ChildStack<RootComponent.Configuration, RootComponent.Child>
) {
    Children(childStack, animation = stackAnimation(slide())) {
        when (val instance = it.instance) {
            is RootComponent.Child.MainMenu -> HomeScreen(instance.component)
            is RootComponent.Child.Settings -> Settings(instance.component)
            is RootComponent.Child.AuthSettings -> AuthSettings(instance.component)
        }
    }
}

@Composable
fun App(root: RootComponent) {
    MaterialTheme(colorScheme = DefaultColorScheme) {
        val childStack by root.childStack.subscribeAsState()
        OptionalAuthLock(
            title = unlockAccountInfo(),
            subtitle = unlockAccountInfoSubtitle(),
            authCancelled = { AppView(childStack) }
        ) {
            AppView(childStack)
        }
    }
}
