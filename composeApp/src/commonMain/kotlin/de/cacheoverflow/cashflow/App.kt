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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.cacheoverflow.cashflow.ui.DefaultColorScheme
import de.cacheoverflow.cashflow.ui.Settings
import de.cacheoverflow.cashflow.utils.DefaultCashFlowSettingsHolder
import de.cacheoverflow.cashflow.utils.ICashFlowSettingsHolder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val settingsModule = module {
    single<ICashFlowSettingsHolder> { DefaultCashFlowSettingsHolder() }
    singleOf(::DefaultCashFlowSettingsHolder)
}

enum class Menu {
    HOME,
    ACCOUNTS,
    SETTINGS
}

@Composable
fun App() {
    var currentMenu by remember { mutableStateOf(Menu.SETTINGS) } // TODO: Change to home
    MaterialTheme(colorScheme = DefaultColorScheme) {
        Box {
            // TODO: Add Home and Accounts
            when(currentMenu) {
                Menu.SETTINGS -> Settings()
                else -> {}
            }
        }
        Box(contentAlignment = Alignment.BottomStart, modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondary)
            ) {
                IconButton(
                    onClick = { currentMenu = Menu.HOME },
                    modifier = Modifier.weight(1.0f),
                    enabled = currentMenu != Menu.HOME
                ) {
                    Icon(
                        Icons.Filled.Home,
                        "Home"
                    )
                }
                IconButton(
                    onClick = { currentMenu = Menu.ACCOUNTS },
                    modifier = Modifier.weight(1.0f),
                    enabled = currentMenu != Menu.ACCOUNTS
                ) {
                    Icon(
                        Icons.Filled.AccountBox,
                        "Home"
                    )
                }
                IconButton(
                    onClick = { currentMenu = Menu.SETTINGS },
                    modifier = Modifier.weight(1.0f),
                    enabled = currentMenu != Menu.SETTINGS
                ) {
                    Icon(
                        Icons.Filled.Settings,
                        "Home"
                    )
                }
            }
        }
    }
}
