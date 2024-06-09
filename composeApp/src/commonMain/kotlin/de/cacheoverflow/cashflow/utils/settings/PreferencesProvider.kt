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

import de.cacheoverflow.cashflow.utils.defaultCoroutineScope
import de.cacheoverflow.cashflow.utils.ioCoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.SYSTEM

class PreferencesProvider(
    pathProvider: (Path) -> Path,
): StateFlow<AppSettings> {
    private val settingsFlow: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings())
    private val fileSystem = FileSystem.SYSTEM
    private val settingsUpdateMutex = Mutex()
    private val configFile = pathProvider(CONFIG_FILE)

    init {
        if (fileSystem.exists(configFile)) {
            ioCoroutineScope.launch {
                fileSystem.read(configFile) {
                    settingsFlow.emit(Json.decodeFromString(readByteArray().decodeToString()))
                    close()
                }
            }
        }
    }

    fun update(updater: (AppSettings) -> AppSettings) {
        defaultCoroutineScope.launch {
            settingsUpdateMutex.withLock(this) {
                val settings = updater(settingsFlow.value)
                settingsFlow.emit(settings)
                fileSystem.write(configFile) {
                    write(Json.encodeToString(AppSettings.serializer(), settings)
                        .encodeToByteArray())
                    flush()
                    close()
                }
            }
        }
    }

    override suspend fun collect(collector: FlowCollector<AppSettings>): Nothing {
        this.settingsFlow.collect(collector)
    }

    override val replayCache: List<AppSettings>
        get() = this.settingsFlow.replayCache

    override val value: AppSettings
        get() = this.settingsFlow.value

    companion object {
        private val CONFIG_FILE = "application_preferences.json".toPath()
    }
}