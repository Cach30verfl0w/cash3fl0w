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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardDoubleArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ColumnScope.SettingsGroup(
    icon: ImageVector? = null,
    name: String,
    content: @Composable ColumnScope.() -> Unit
) {
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
fun ColumnScope.CollapsableSetting(
    name: String,
    content: @Composable ColumnScope.() -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    Surface(
        color = Color.Transparent
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { isVisible = !isVisible }.height(50.dp)
            ) {
                Text(
                    text = name,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(Modifier.weight(1f))
            }
            AnimatedVisibility(visible = isVisible) {
                Box(
                    modifier = Modifier.background(
                        MaterialTheme.colorScheme.tertiary,
                        RoundedCornerShape(5.dp)
                    )
                ) {
                    Column(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
                        content()
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.RadioCheckSetting(
    name: String,
    checked: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f).height(50.dp)
        ) {
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(Modifier.weight(1.0f))
            RadioButton(checked, onClick = { onClick() })
        }
    }
}

@Composable
fun ColumnScope.ClickSetting(
    name: String,
    onClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(50.dp)) {
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(Modifier.weight(1.0f))
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
    onToggle: (Boolean) -> Unit = {},
    state: MutableState<Boolean>
) {
    Surface(
        color = Color.Transparent,
        onClick = {
            state.value = !state.value
            onToggle(state.value)
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(50.dp)) {
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(Modifier.weight(1.0f))
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