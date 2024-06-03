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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import de.cacheoverflow.cashflow.ui.components.SettingsGroup
import de.cacheoverflow.cashflow.utils.settings

class SettingsComponent(
    private val context: ComponentContext,
    internal val onBack: () -> Unit
): ComponentContext by context

@Composable
fun Settings(component: SettingsComponent) {
    View(settings(), onButton = component.onBack) {
        SettingsGroup("Sicherheit", Icons.Filled.Security) {
            Text("Das hier ist ein Text")
        }
    }
}