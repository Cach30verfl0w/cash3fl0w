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

package de.cacheoverflow.cashflow.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import de.cacheoverflow.cashflow.security.ISecurityProvider
import de.cacheoverflow.cashflow.ui.View
import de.cacheoverflow.cashflow.ui.components.ClickSetting
import de.cacheoverflow.cashflow.ui.components.SettingsGroup
import de.cacheoverflow.cashflow.ui.components.SwitchSetting
import de.cacheoverflow.cashflow.utils.DI
import de.cacheoverflow.cashflow.utils.authenticationSettings
import de.cacheoverflow.cashflow.utils.disableScreenshots
import de.cacheoverflow.cashflow.utils.security
import de.cacheoverflow.cashflow.utils.settings
import de.cacheoverflow.cashflow.utils.settings.PreferencesProvider

class SettingsComponent(
    private val context: ComponentContext,
    internal val onBack: () -> Unit,
    internal val changeToAuthSettings: () -> Unit
) : ComponentContext by context

@Composable
fun Settings(component: SettingsComponent) {
    val securityProvider = DI.inject<ISecurityProvider>()
    val settings = DI.inject<PreferencesProvider>()
    val settingsState by settings.collectAsState()
    View(settings(), onButton = component.onBack) {
        Column {
            SettingsGroup(security(), Icons.Filled.DisplaySettings) {
                SwitchSetting(
                    disableScreenshots(),
                    enabled = securityProvider.isScreenshotPolicySupported(),
                    value = settingsState.screenshotsDisabled
                ) {
                    securityProvider.toggleScreenshotPolicy()
                    settings.update {
                        it.copy(screenshotsDisabled = securityProvider.areScreenshotsDisallowed())
                    }
                }
                ClickSetting(authenticationSettings()) {
                    component.changeToAuthSettings()
                }
            }
        }
    }
}