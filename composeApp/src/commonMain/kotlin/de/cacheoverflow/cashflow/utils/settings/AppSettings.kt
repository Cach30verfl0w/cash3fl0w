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

package de.cacheoverflow.cashflow.utils.settings

import androidx.compose.ui.text.intl.Locale
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

/**
 * This class holds all raw settings of the application. These settings are used to store and
 * manage the settings of this application.
 *
 * @author Cedric Hammes
 * @since  04/06/2024
 */
@Serializable
data class AppSettings(
    val theme: EnumTheme = EnumTheme.SYSTEM,
    val language: EnumLanguage = EnumLanguage.entries
        .firstOrNull { it.name == Locale.current.language.uppercase() } ?: EnumLanguage.EN,
    val screenshotsDisabled: Boolean = false,
    val authenticationRequired: Boolean = false
) {
    /**
     * This enum represents the theme used by the application. The user can use whether system to
     * adopt the theme from the system or the light theme or the dark theme.
     *
     * @author Cedric Hammes
     * @since  01/06/2024
     */
    @Serializable
    enum class EnumTheme {
        SYSTEM,
        LIGHT,
        DARK;

        override fun toString(): String {
            return when(this) {
                SYSTEM -> "System"
                LIGHT -> "Light"
                DARK -> "Dark"
            }
        }
    }

    /**
     * This enum represents the language use by the application. The user can choose between English
     * and German. By default, the language configured for the system (if not available, english is
     * used as fallback) is being configured.
     *
     * @author Cedric Hammes
     * @since  01/06/2024
     */
    @Serializable
    enum class EnumLanguage {
        DE,
        EN;

        override fun toString(): String {
            return when(this) {
                DE -> "Deutsch"
                EN -> "English"
            }
        }
    }

    /**
     * This interface implements the holder of the settings data class. This is simply used for the
     * dependency injection, provided through Koin. The application updates the settings by the
     * update method specified.
     *
     * @author Cedric Hammes
     * @since  01/06/2024
     */
    interface ICashFlowSettingsHolder: StateFlow<AppSettings> {
        fun update(updater: (AppSettings) -> AppSettings)
    }
}