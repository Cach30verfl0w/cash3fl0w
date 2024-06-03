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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.cacheoverflow.cashflow.utils.grayOutIfDisabled

@Composable
fun SwitchSetting(name: String, enabled: Boolean = true, value: Boolean, onSwitch: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text(name, fontSize = 22.sp)
        }
        Spacer(Modifier.weight(1.0f))
        Switch(checked = value, onCheckedChange = {
            if (enabled) {
                onSwitch()
            }
        }, colors = SwitchDefaults.colors().grayOutIfDisabled(enabled))
    }
}

/**
 * This component represents a group of settings in a settings menu of this application. The
 * settings are grouped to provide a better oversight over all settings available.
 *
 * @param name    The name of the settings group
 * @param icon    The icon of the settings group
 * @param content The content of the settings group
 *
 * @author Cedric Hammes
 * @since  03/06/2024
 */
@Composable
fun SettingsGroup(name: String, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null)
            Spacer(Modifier.width(5.dp))
            Text(name, fontSize = 30.sp)
        }
        Spacer(Modifier.height(5.dp))
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 12.dp, bottom = 12.dp)) {
                content()
            }
        }
    }
}