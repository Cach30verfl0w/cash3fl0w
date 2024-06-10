/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import de.cacheoverflow.cashflow.security.ISecurityProvider
import de.cacheoverflow.cashflow.ui.View
import de.cacheoverflow.cashflow.ui.components.CheckboxSetting
import de.cacheoverflow.cashflow.ui.components.SettingsGroup
import de.cacheoverflow.cashflow.ui.components.SwitchSetting
import de.cacheoverflow.cashflow.utils.DI
import de.cacheoverflow.cashflow.utils.authenticationEnabled
import de.cacheoverflow.cashflow.utils.authenticationSettings
import de.cacheoverflow.cashflow.utils.biometricAuth
import de.cacheoverflow.cashflow.utils.credentialAuth
import de.cacheoverflow.cashflow.utils.keyringNotSecured
import de.cacheoverflow.cashflow.utils.keyringNotUnlocked
import de.cacheoverflow.cashflow.utils.keyringSecured
import de.cacheoverflow.cashflow.utils.keyringUnlocked
import de.cacheoverflow.cashflow.utils.settings
import de.cacheoverflow.cashflow.utils.settings.AppSettings
import de.cacheoverflow.cashflow.utils.settings.PreferencesProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest

class AuthSettingsComponent(
    private val context: ComponentContext,
    internal val onBack: () -> Unit
): ComponentContext by context

@Composable
fun AuthStatus(ifTrueText: String, ifFalseText: String, value: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (value) {
            Icon(Icons.Filled.Check, null, tint = MaterialTheme.colorScheme.primary)
            Text(ifTrueText, color = MaterialTheme.colorScheme.primary)
        } else {
            Icon(Icons.Filled.Close, null, tint = MaterialTheme.colorScheme.error)
            Text(ifFalseText, color = MaterialTheme.colorScheme.error)
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun AuthSettings(component: AuthSettingsComponent) {
    val securityProvider = DI.inject<ISecurityProvider>()
    val isAuthenticated by securityProvider.wasAuthenticated().collectAsState()
    val settings = DI.inject<PreferencesProvider>()
    val authSettingsState by settings.mapLatest { it.authenticationSettings }
        .collectAsState(AppSettings.AuthenticationSettings())

    View(authenticationSettings(), onButton = component.onBack) {
        Column {
            SettingsGroup(
                "Status",
                Icons.Filled.QueryStats,
                innerModifier = Modifier.padding(start = 12.dp)
            ) {
                // TODO: Show keyring only as unlocked if the account keys were be loaded
                //   and authentication was enabled in settings (If authentication stays
                //   optional)
                AuthStatus(keyringSecured(), keyringNotSecured(), true)
                AuthStatus(keyringUnlocked(), keyringNotUnlocked(), isAuthenticated)
            }
            SettingsGroup(settings(), Icons.Filled.Settings) {
                SwitchSetting(authenticationEnabled(), authSettingsState.authenticationEnabled) {
                    settings.update {
                        it.copy(authenticationSettings = it.authenticationSettings.copy(
                            authenticationEnabled = !authSettingsState.authenticationEnabled
                        ))
                    }
                }
                CheckboxSetting(credentialAuth(), authSettingsState.credentialAuthentication) {
                    settings.update {
                        it.copy(authenticationSettings = it.authenticationSettings.copy(
                            credentialAuthentication = !authSettingsState.credentialAuthentication
                        ))
                    }
                }
                CheckboxSetting(biometricAuth(), authSettingsState.biometricAuthentication) {
                    settings.update {
                        it.copy(authenticationSettings = it.authenticationSettings.copy(
                            biometricAuthentication = !authSettingsState.biometricAuthentication
                        ))
                    }
                }
            }
        }
    }
}