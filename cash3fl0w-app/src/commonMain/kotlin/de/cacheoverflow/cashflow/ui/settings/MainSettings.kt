/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.Style
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ComponentContext
import de.cacheoverflow.cashflow.security.ISecurityProvider
import de.cacheoverflow.cashflow.ui.View
import de.cacheoverflow.cashflow.ui.components.ClickSetting
import de.cacheoverflow.cashflow.ui.components.SelectableSetting
import de.cacheoverflow.cashflow.ui.components.SettingsGroup
import de.cacheoverflow.cashflow.ui.components.SwitchSetting
import de.cacheoverflow.cashflow.utils.DI
import de.cacheoverflow.cashflow.utils.appearance
import de.cacheoverflow.cashflow.utils.authenticationSettings
import de.cacheoverflow.cashflow.utils.disableScreenshots
import de.cacheoverflow.cashflow.utils.language
import de.cacheoverflow.cashflow.utils.security
import de.cacheoverflow.cashflow.utils.settings
import de.cacheoverflow.cashflow.utils.settings.PreferencesProvider
import de.cacheoverflow.cashflow.utils.theme

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
            SettingsGroup(appearance(), Icons.Filled.Style) {
                SelectableSetting(theme(), value = settingsState.theme) { newTheme ->
                    settings.update {
                        it.copy(theme = newTheme)
                    }
                }
                SelectableSetting(language(), value = settingsState.language) { newLanguage ->
                    settings.update {
                        it.copy(language = newLanguage)
                    }
                }
            }
        }
    }
}