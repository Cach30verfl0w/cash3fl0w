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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QueryStats
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import de.cacheoverflow.cashflow.security.ISecurityProvider
import de.cacheoverflow.cashflow.ui.View
import de.cacheoverflow.cashflow.ui.components.Modal
import de.cacheoverflow.cashflow.ui.components.ModalType
import de.cacheoverflow.cashflow.ui.components.SettingsGroup
import de.cacheoverflow.cashflow.utils.DI
import de.cacheoverflow.cashflow.utils.algorithmUsed
import de.cacheoverflow.cashflow.utils.authenticationSettings
import de.cacheoverflow.cashflow.utils.keyringNotSecured
import de.cacheoverflow.cashflow.utils.keyringNotUnlocked
import de.cacheoverflow.cashflow.utils.keyringSecured
import de.cacheoverflow.cashflow.utils.keyringUnlocked
import de.cacheoverflow.cashflow.utils.rsaExplanation

class AuthSettingsComponent(
    private val context: ComponentContext,
    internal val onBack: () -> Unit
): ComponentContext by context

// TODO: Lock authentication settings behind asymmetric encryption. So the device can always access
//   the settings but can only change them with legitimation through authentication.

@Composable
fun AutoInfo(text: String, modalTitle: String, modalContent: @Composable ColumnScope.() -> Unit) {
    val showModal = remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
        showModal.value = true
    }) {
        Icon(Icons.Filled.Info, null, tint = MaterialTheme.colorScheme.onSecondary)
        Text(text, color = MaterialTheme.colorScheme.onSecondary)
    }
    Modal(showModal, title = modalTitle, type = ModalType.INFO, content = modalContent)
}

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

@Composable
fun AuthSettings(component: AuthSettingsComponent) {
    val securityProvider = DI.inject<ISecurityProvider>()
    val asymmetricProvider = securityProvider.getAsymmetricCryptoProvider()
    val isAuthenticated by securityProvider.wasAuthenticated().collectAsState()
    View(authenticationSettings(), onButton = component.onBack) {
        SettingsGroup(
            "Status",
            Icons.Filled.QueryStats,
            innerModifier = Modifier.padding(start = 12.dp)
        ) {
            AuthStatus(keyringSecured(), keyringNotSecured(), true)
            AuthStatus(keyringUnlocked(), keyringNotUnlocked(), isAuthenticated)
            AutoInfo(algorithmUsed(asymmetricProvider.getName()), asymmetricProvider.getName()) {
                Text(rsaExplanation(), color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}