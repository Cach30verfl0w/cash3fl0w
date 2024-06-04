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

import de.cacheoverflow.cashflow.utils.security.AbstractSecurityProvider
import de.cacheoverflow.cashflow.utils.DI
import de.cacheoverflow.cashflow.utils.collectAsync
import de.cacheoverflow.cashflow.utils.defaultCoroutineScope
import de.cacheoverflow.cashflow.utils.security.IKey
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okio.FileSystem
import okio.Path
import okio.SYSTEM

class PreferencesProvider(
    pathProvider: (String) -> Path,
    private val securityProvider: AbstractSecurityProvider = DI.inject(),
    private val settingsFlow: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings()),
    private val publicKeyFlow: MutableStateFlow<IKey?> = MutableStateFlow(null),
    private val privateKeyFlow: MutableStateFlow<IKey?> = MutableStateFlow(null),
    fileSystem: FileSystem = FileSystem.SYSTEM,
    private val settingsUpdateMutex: Mutex = Mutex(),
    private val pubkeyFile: Path = pathProvider(PUBKEY_FILE)
): StateFlow<AppSettings> {
    init {
        if (fileSystem.exists(pubkeyFile)) {
            securityProvider.readKey(fileSystem, pubkeyFile).collectAsync {
                publicKeyFlow.emit(it)
            }
        }

        // Update private key and update public key if public key doesn't need to be created before
        securityProvider.wasAuthenticated().collectAsync { isAuthenticated ->
            if (isAuthenticated) {
                if (publicKeyFlow.value != null) {
                    securityProvider
                        .getOrCreateKey(KEY_NAME, IKey.EnumAlgorithm.RSA).collect {
                            fileSystem.write(pubkeyFile) {
                                write(it.raw())
                                flush()
                                close()
                            }
                            publicKeyFlow.emit(it)
                        }
                }
                securityProvider
                    .getOrCreateKey(KEY_NAME, IKey.EnumAlgorithm.RSA, privateKey = true)
                    .collect {
                        privateKeyFlow.emit(it)
                    }
            }
        }
    }

    fun update(updater: (AppSettings) -> AppSettings) {
        defaultCoroutineScope.launch {
            settingsUpdateMutex.withLock(this) {
                val settings = updater(settingsFlow.value)
                settingsFlow.emit(settings)
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
        private const val KEY_NAME = "app_preferences_lock"
        private const val PUBKEY_FILE = "application_preferences.pubkey"
    }
}