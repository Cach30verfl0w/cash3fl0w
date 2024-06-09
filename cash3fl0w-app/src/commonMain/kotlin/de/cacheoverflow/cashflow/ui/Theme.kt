/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
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
import de.cacheoverflow.cashflow.utils.settings.AppSettings
import de.cacheoverflow.cashflow.utils.settings.PreferencesProvider
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
            tertiary = Color(0xFFC3C9E0),
            onTertiary = Color(0xFF040404),
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
            outline = Color(0xFF79D07D),
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
            secondary = Color(0xff2d3436),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFF666666),
            onSecondaryContainer = Color(0xFFE6E6E6),
            tertiary = Color(0xFF505D62),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFF666666),
            onTertiaryContainer = Color(0xFFE6E6E6),
            error = Color(0xFFCE0F27),
            errorContainer = Color(0xFFFFDAD6),
            onError = Color(0xFFFFFFFF),
            onErrorContainer = Color(0xFF45002),
            background = Color(0xFF121212),
            onBackground = Color(0xFFFFFFFF),
            surface = Color(0xff02500),
            onSurface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFF214152),
            onSurfaceVariant = Color(0xFFFFFFFF),
            outline = Color(0xFF79D07D),
            inverseOnSurface = Color(0xFF161616)
        )
    }

val DefaultColorScheme: ColorScheme
    @Composable
    get() {
        val settings by getKoin().get<PreferencesProvider>().collectAsState()
        return when (settings.theme) {
            AppSettings.EnumTheme.SYSTEM -> if (isSystemInDarkTheme())
                DarkColorScheme
            else
                LightColorScheme
            AppSettings.EnumTheme.LIGHT -> LightColorScheme
            AppSettings.EnumTheme.DARK -> DarkColorScheme
        }
    }