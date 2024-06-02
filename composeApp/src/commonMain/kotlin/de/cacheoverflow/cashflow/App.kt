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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import de.cacheoverflow.cashflow.ui.DefaultColorScheme
import de.cacheoverflow.cashflow.ui.Settings
import de.cacheoverflow.cashflow.ui.components.RootComponent
import de.cacheoverflow.cashflow.utils.DefaultCashFlowSettingsHolder
import de.cacheoverflow.cashflow.utils.ICashFlowSettingsHolder
import de.cacheoverflow.cashflow.utils.defaultCoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val settingsModule = module {
    single<ICashFlowSettingsHolder> { DefaultCashFlowSettingsHolder() }
    singleOf(::DefaultCashFlowSettingsHolder)
}

class MainMenuComponent(context: ComponentContext): ComponentContext by context {
    val menu = MutableStateFlow(EnumMenu.HOME)

    fun changeMenuTo(newMenu: EnumMenu) {
        defaultCoroutineScope.launch {
            menu.emit(newMenu)
        }
    }

    enum class EnumMenu {
        HOME,
        ACCOUNTS,
        SETTINGS
    }
}

@Composable
fun MainMenu(component: MainMenuComponent) {
    val currentMenu by component.menu.collectAsState()
    Box {
        // TODO: Add Home and Accounts
        when(currentMenu) {
            MainMenuComponent.EnumMenu.SETTINGS -> Settings()
            else -> {}
        }
    }
    Box(contentAlignment = Alignment.BottomStart, modifier = Modifier.fillMaxSize()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondary)
        ) {
            IconButton(
                onClick = { component.changeMenuTo(MainMenuComponent.EnumMenu.HOME) },
                modifier = Modifier.weight(1.0f),
                enabled = currentMenu != MainMenuComponent.EnumMenu.HOME
            ) {
                Icon(
                    Icons.Filled.Home,
                    "Home"
                )
            }
            IconButton(
                onClick = { component.changeMenuTo(MainMenuComponent.EnumMenu.ACCOUNTS) },
                modifier = Modifier.weight(1.0f),
                enabled = currentMenu != MainMenuComponent.EnumMenu.ACCOUNTS
            ) {
                Icon(
                    Icons.Filled.AccountBox,
                    "Accounts"
                )
            }
            IconButton(
                onClick = { component.changeMenuTo(MainMenuComponent.EnumMenu.SETTINGS) },
                modifier = Modifier.weight(1.0f),
                enabled = currentMenu != MainMenuComponent.EnumMenu.SETTINGS
            ) {
                Icon(
                    Icons.Filled.Settings,
                    "Settings"
                )
            }
        }
    }
}

@Composable
fun App(root: RootComponent) {
    MaterialTheme(colorScheme = DefaultColorScheme) {
        val childStack by root.childStack.subscribeAsState()
        Children(stack = childStack, animation = stackAnimation(slide())) {
            when(val instance = it.instance) {
                is RootComponent.Child.MainMenu -> MainMenu(instance.component)
            }
        }
    }
}
