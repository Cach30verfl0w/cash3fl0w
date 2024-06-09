/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.utils.settings

import androidx.compose.ui.text.intl.Locale
import de.cacheoverflow.cashflow.utils.dark
import de.cacheoverflow.cashflow.utils.light
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
                LIGHT -> light()
                DARK -> dark()
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
        EN,
        PL;

        override fun toString(): String {
            return when(this) {
                DE -> "Deutsch"
                EN -> "English"
                PL -> "Polski"
            }
        }
    }
}