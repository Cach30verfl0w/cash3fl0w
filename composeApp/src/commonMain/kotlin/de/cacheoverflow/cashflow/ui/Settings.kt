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

package de.cacheoverflow.cashflow.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Screenshot
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.cacheoverflow.cashflow.ui.components.SettingsGroup
import de.cacheoverflow.cashflow.ui.components.ToggleSetting
import de.cacheoverflow.cashflow.utils.ICashFlowSettingsHolder
import org.koin.compose.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings() {
    val boolState = remember { mutableStateOf(true) }
    val settings = getKoin().get<ICashFlowSettingsHolder>()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }, colors = TopAppBarDefaults.topAppBarColors()
                .copy(containerColor = MaterialTheme.colorScheme.primary))
        }
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(it).padding(16.dp)
        ) {
            SettingsGroup(icon = Icons.Filled.Security, name = "Security") {
                ToggleSetting(
                    name = "Block screenshots",
                    description = "Disable in-app screenshot to protect bank/financial " +
                            "information. This feature is only working on mobile devices.",
                    state = boolState,
                    icon = Icons.Filled.Screenshot,
                    onToggle = {
                        settings.update { settings -> settings.copy(screenshotsEnabled = !it) }
                    }
                )
            }
        }
    }
}