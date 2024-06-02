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

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import de.cacheoverflow.cashflow.utils.EnumTheme
import de.cacheoverflow.cashflow.utils.ICashFlowSettingsHolder
import org.koin.compose.getKoin

val LightColorScheme: ColorScheme
    @Composable
    get() {
        return lightColorScheme(
            primary = Color(0xFF11CC1A),
            onPrimary = Color(0xFF040404),
            primaryContainer = Color(0xff5bcc60),
            onPrimaryContainer = Color(0xFF212121),
            secondary = Color(0xFFE0E8FF),
            onSecondary = Color(0xFF101010), 
            secondaryContainer = Color(0xFF666666),
            onSecondaryContainer = Color(0xFFE6E6E6),
            tertiary = Color(0xffc3c9e0),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFF666666),
            onTertiaryContainer = Color(0xFFE6E6E6),
            error = Color(0xFF8A0D1D),
            errorContainer = Color(0xFFFFDAD6),
            onError = Color(0xFFFFFFFF),
            onErrorContainer = Color(0xFF45002),
            background = Color(0xFFFFFFFF),
            onBackground = Color(0xFF000000),
            surface = Color(0xFFFCFEFE),
            onSurface = Color(0xFF121212),
            surfaceVariant = Color(0xFF214152),
            onSurfaceVariant = Color(0xFF222222),
            outline = Color(0xff79d07d),
            inverseOnSurface = Color(0xFFD6F6FF),
            inverseSurface = Color(0xFF00363F),
        )
    }

val DarkColorScheme: ColorScheme
    @Composable
    get() {
        return darkColorScheme(
            primary = Color(0xff70ee56),
            onPrimary = Color(0xFF040404),
            primaryContainer = Color(0xff5bcc60),
            onPrimaryContainer = Color(0xFFEEEEEE),
            secondary = Color(0xFF384144),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFF666666),
            onSecondaryContainer = Color(0xFFE6E6E6),
            tertiary = Color(0xff505d62),
            onTertiary = Color(0xFF101010),
            tertiaryContainer = Color(0xFF666666),
            onTertiaryContainer = Color(0xFFE6E6E6),
            error = Color(0xFF8A0D1D),
            errorContainer = Color(0xFFFFDAD6),
            onError = Color(0xFFFFFFFF),
            onErrorContainer = Color(0xFF45002),
            background = Color(0xFF121212),
            onBackground = Color(0xFFFFFFFF),
            surface = Color(0xff02500),
            onSurface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFF214152),
            onSurfaceVariant = Color(0xFFFFFFFF),
            outline = Color(0xff79d07d),
            inverseOnSurface = Color(0xFF161616)
        )
    }

val DefaultColorScheme: ColorScheme
    @Composable
    get() {
        val settings by getKoin().get<ICashFlowSettingsHolder>().collectAsState()
        return when (settings.theme) {
            EnumTheme.SYSTEM -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
            EnumTheme.LIGHT -> LightColorScheme
            EnumTheme.DARK -> DarkColorScheme
        }
    }