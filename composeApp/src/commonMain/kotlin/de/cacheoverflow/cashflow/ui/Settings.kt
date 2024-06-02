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
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.cacheoverflow.cashflow.ui.components.RadioCheckSetting
import de.cacheoverflow.cashflow.ui.components.CollapsableSetting
import de.cacheoverflow.cashflow.ui.components.SettingsGroup
import de.cacheoverflow.cashflow.ui.components.ToggleSetting
import de.cacheoverflow.cashflow.utils.ICashFlowSettingsHolder
import de.cacheoverflow.cashflow.utils.disableScreenshots
import de.cacheoverflow.cashflow.utils.security
import de.cacheoverflow.cashflow.utils.settings
import org.koin.compose.getKoin

/**
 * This function represents the view of the settings available. This composable is based on the
 * router to make navigation through the settings possible.
 *
 * @author Cedric Hammes
 * @since  01/06/2024
 */
@Composable
fun Settings() {
    MainSettings()
}

/**
 * This function represents the main display that provides an oversight of all categories of
 * settings available in the CashFlow app/application. This menu is the first shown to the user in
 * the settings category of the application.
 *
 * TODO: Add clickable setting for theme and then redirect to extra window
 * TODO: Add clickable setting for security settings with screenshot restriction etc.
 * TODO: Add clickable setting for software information with credits
 *
 * @author Cedric Hammes
 * @since  01/06/2024
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSettings() {
    val settings = getKoin().get<ICashFlowSettingsHolder>()
    val settingsState by settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = settings(),
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
            SettingsGroup(icon = Icons.Filled.Security, name = security()) {
                ToggleSetting(
                    name = disableScreenshots(),
                    checked = settingsState.screenshotsDisabled,
                    onToggle = {
                        settings.update { settings ->
                            settings.copy(screenshotsDisabled = !settings.screenshotsDisabled)
                        }
                    }
                )
                CollapsableSetting(name = disableScreenshots()) {
                    RadioCheckSetting("A", true, onClick = {})
                    RadioCheckSetting("B", true, onClick = {})
                    RadioCheckSetting("C", true, onClick = {})
                }
            }
        }
    }
}