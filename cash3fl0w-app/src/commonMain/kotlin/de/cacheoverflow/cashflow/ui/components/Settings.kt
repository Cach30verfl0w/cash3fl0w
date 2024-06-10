/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.cacheoverflow.cashflow.utils.deriveHSLIf
import de.cacheoverflow.cashflow.utils.grayOutIfDisabled

object SettingsGroupScope

@Composable
inline fun <reified T: Enum<T>> SettingsGroupScope.SelectableSetting(
    name: String,
    value: T,
    enabled: Boolean = true,
    expanded: Boolean = false,
    crossinline onChange: (T) -> Unit
) {
    var isExpanded by remember { mutableStateOf(enabled && expanded) }
    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.run {
            if (enabled) { this.clickable { isExpanded = !isExpanded } } else { this }.height(40.dp)
        }) {
            Text(
                name,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSecondary
                    .deriveHSLIf(!enabled, lightness = 0.5f)
            )
            Spacer(Modifier.weight(1f))
            Icon(
                if (isExpanded) {
                    Icons.Filled.ArrowDropUp
                } else {
                    Icons.Filled.ArrowDropDown
                   }, null,
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(end = 10.dp)
            )
        }
        Spacer(Modifier.height(5.dp))
        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(5.dp))
            ) {
                for (enumValue in enumValues<T>()) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {
                        onChange(enumValue)
                    }) {
                        Text(
                            enumValue.toString(),
                            color = MaterialTheme.colorScheme.onTertiary,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Spacer(Modifier.weight(1f))
                        RadioButton(enumValue == value, onClick = { onChange(enumValue) })
                    }
                }
            }
        }
    }
}

/**
 * This component represents a setting that can be clicked to switch the menu etc.
 *
 * @param name    The name of the setting
 * @param enabled Whether this setting is enabled (clickable or not)
 * @param onClick Function called when element gets clicked
 *
 * @author Cedric Hammes
 * @since  04/06/2024
 */
@Composable
fun SettingsGroupScope.ClickSetting(
    name: String,
    enabled: Boolean = true,
    textColor: Color = MaterialTheme.colorScheme.onSecondary,
    onClick: () -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp).run {
            if (enabled) {
                this.clickable { onClick() }
            } else {
                this
            }
        }
    ) {
        Column {
            Text(
                name,
                fontSize = 22.sp,
                color = textColor.deriveHSLIf(!enabled, lightness = 0.5f)
            )
        }
        Spacer(Modifier.weight(1.0f))
        IconButton(onClick = {
            if (enabled) {
                onClick()
            }
        }) {
            Icon(
                Icons.Filled.KeyboardDoubleArrowRight,
                null,
                tint = textColor.deriveHSLIf(!enabled, lightness = 0.5f)
            )
        }
    }
}

/**
 * This component represents a setting that can be toggled and the state is represented through a
 * radio button.
 *
 * @param name     The name of the setting
 * @param enabled  Whether the setting is enabled (toggleable) or not
 * @param value    The value of the setting (is the setting toggled or not)
 * @param onToggle Function called when element gets clicked
 *
 * @author Cedric Hammes
 * @since  04/06/2024
 */
@Composable
fun SettingsGroupScope.RadioSetting(
    name: String,
    enabled: Boolean = true,
    value: Boolean,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp).run {
            if (enabled) {
                this.clickable { onToggle() }
            } else {
                this
            }
        }
    ) {
        Column {
            Text(
                name,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSecondary
                    .deriveHSLIf(!enabled, lightness = 0.5f)
            )
        }
        Spacer(Modifier.weight(1.0f))
        RadioButton(selected = value, onClick = {
            if (enabled) {
                onToggle()
            }
        }, colors = RadioButtonDefaults.colors().grayOutIfDisabled(enabled))
    }
}

/**
 * This component represents a setting that can be toggled and the state is represented through a
 * checkbox.
 *
 * @param name     The name of the setting
 * @param enabled  Whether the setting is enabled (toggleable) or not
 * @param value    The value of the setting (is the setting toggled or not)
 * @param onToggle Function called when element gets clicked
 *
 * @author Cedric Hammes
 * @since  04/06/2024
 */
@Composable
fun SettingsGroupScope.CheckboxSetting(
    name: String,
    value: Boolean,
    enabled: Boolean = true,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp).run {
            if (enabled) {
                this.clickable { onToggle() }
            } else {
                this
            }
        }
    ) {
        Column {
            Text(
                name,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSecondary
                    .deriveHSLIf(!enabled, lightness = 0.5f)
            )
        }
        Spacer(Modifier.weight(1.0f))
        Checkbox(enabled = enabled, checked = value, onCheckedChange = {
            if (enabled) {
                onToggle()
            }
        }, colors = CheckboxDefaults.colors().grayOutIfDisabled(enabled))
    }
}

/**
 * This component represents a setting that can be toggled and the state is represented through a
 * switch.
 *
 * @param name     The name of the setting
 * @param enabled  Whether the setting is enabled (toggleable) or not
 * @param value    The value of the setting (is the setting toggled or not)
 * @param onToggle Function called when element gets clicked
 *
 * @author Cedric Hammes
 * @since  04/06/2024
 */
@Composable
fun SettingsGroupScope.SwitchSetting(
    name: String,
    value: Boolean,
    enabled: Boolean = true,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 10.dp, end = 10.dp).run {
            if (enabled) {
                this.clickable { onToggle() }
            } else {
                this
            }
        }
    ) {
        Column {
            Text(
                name,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSecondary
                    .deriveHSLIf(!enabled, lightness = 0.5f)
            )
        }
        Spacer(Modifier.weight(1.0f))
        Switch(enabled = enabled, checked = value, onCheckedChange = {
            if (enabled) {
                onToggle()
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
fun ColumnScope.SettingsGroup(
    name: String?,
    icon: ImageVector?,
    innerModifier: Modifier = Modifier,
    content: @Composable SettingsGroupScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
    ) {
        if (name != null && icon != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
                Spacer(Modifier.width(5.dp))
                Text(name, fontSize = 30.sp, color = MaterialTheme.colorScheme.onBackground)
            }
            Spacer(Modifier.height(5.dp))
        }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .fillMaxWidth()
        ) {
            Column(modifier = innerModifier.padding(top = 12.dp, bottom = 12.dp)) {
                content(SettingsGroupScope)
            }
        }
    }
}