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

package de.cacheoverflow.cashflow.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsGroup(icon: ImageVector? = null, name: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row {
            if (icon != null) {
                Icon(icon, contentDescription = null)
            }
            Text(
                text = name,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Surface(
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp).fillMaxHeight()) {
                content()
            }
        }
    }
}

@Composable
fun ClickSetting(
    name: String,
    description: String? = null,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    iconDescription: String? = null
) {
    val showModal = remember { mutableStateOf(false) }
    Surface(
        color = Color.Transparent,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = iconDescription,
                    modifier = Modifier.size(25.dp),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 16.dp),
            )
            if (description != null) {
                IconButton(
                    onClick = {
                        showModal.value = !showModal.value
                    }
                ) {
                    Icon(Icons.Filled.Info, "Information")
                }
                Modal(title = name, type = ModalType.INFO, visible = showModal) {
                    Text(description)
                }
            }
            Spacer(modifier = Modifier.weight(1.0f))
            Icon(
                Icons.Rounded.KeyboardDoubleArrowRight,
                tint = MaterialTheme.colorScheme.onSecondary,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ToggleSetting(
    name: String,
    description: String? = null,
    onToggle: (Boolean) -> Unit = {},
    icon: ImageVector? = null,
    iconDescription: String? = null,
    state: MutableState<Boolean>
) {
    val showModal = remember { mutableStateOf(false) }
    Surface(
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            state.value = !state.value
            onToggle(state.value)
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = iconDescription,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 16.dp),
            )
            if (description != null) {
                IconButton(
                    onClick = {
                        showModal.value = !showModal.value
                    }
                ) {
                    Icon(
                        Icons.Filled.Info,
                        "Information",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Modal(title = name, type = ModalType.INFO, visible = showModal) {
                    Text(description)
                }
            }
            Spacer(Modifier.weight(1f))
            Switch(
                checked = state.value,
                onCheckedChange = {
                    state.value = it
                    onToggle(it)
                }
            )
        }
    }
}