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

package de.cacheoverflow.cashflow.utils

import androidx.compose.ui.text.intl.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable

val defaultCoroutineScope = CoroutineScope(Dispatchers.Default)

/**
 * This enum represents the theme used by the application. The user can use whether system to adopt
 * the theme from the system or the light theme or the dark theme.
 *
 * @author Cedric Hammes
 * @since  01/06/2024
 */
@Serializable
enum class EnumTheme {
    SYSTEM,
    LIGHT,
    DARK
}

/**
 * This enum represents the language use by the application. The user can choose between English and
 * German. By default, the language configured for the system (if not available, english is used as
 * fallback) is being configured.
 *
 * @author Cedric Hammes
 * @since  01/06/2024
 */
@Serializable
enum class EnumLanguage {
    DE,
    EN
}

/**
 * This class holds all raw settings of the application. These settings are used by the app to
 * configure non-critical information like theme and language. Below this text you can see all
 * settings available:
 * - Language used by the application
 * - Theme used by the application
 *
 * @author Cedric Hammes
 * @since  01/06/2024
 */
@Serializable
data class CashFlowSettings(
    val theme: EnumTheme = EnumTheme.SYSTEM,
    val language: EnumLanguage = EnumLanguage.entries
        .firstOrNull { it.name == Locale.current.language.uppercase() } ?: EnumLanguage.EN,
    val screenshotsDisabled: Boolean = false
)

/**
 * This interface implements the holder of the settings data class. This is simply used for the
 * dependency injection, provided through Koin. The application updates the settings by the update
 * method specified.
 *
 * @author Cedric Hammes
 * @since  01/06/2024
 */
interface ICashFlowSettingsHolder: StateFlow<CashFlowSettings> {
    fun update(updater: (CashFlowSettings) -> CashFlowSettings)
}

/**
 * This class is the implementation of the settings holder used by the dependency injection library
 * Koin.
 *
 * @author Cedric Hames
 * @since  01/06/2024
 */
class DefaultCashFlowSettingsHolder(
    private val flow: MutableStateFlow<CashFlowSettings> = MutableStateFlow(
        injectKoin<IPreferencesProvider>().readSettings()
    )
): ICashFlowSettingsHolder {
    private val updateMutex = Mutex()

    override suspend fun collect(collector: FlowCollector<CashFlowSettings>): Nothing {
        this.flow.collect(collector)
    }

    override fun update(updater: (CashFlowSettings) -> CashFlowSettings) {
        defaultCoroutineScope.launch {
            updateMutex.withLock(this) {
                val settings = updater(flow.value)
                println(settings)
                injectKoin<IPreferencesProvider>().writeSettings(settings)
                flow.emit(settings)
            }
        }
    }

    override val replayCache: List<CashFlowSettings>
        get() = flow.replayCache
    override val value: CashFlowSettings
        get() = flow.value
}
