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

import de.cacheoverflow.cashflow.security.IKey
import de.cacheoverflow.cashflow.security.ISecurityProvider
import de.cacheoverflow.cashflow.utils.DI
import de.cacheoverflow.cashflow.utils.collectAsync
import de.cacheoverflow.cashflow.utils.defaultCoroutineScope
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
    private val securityProvider: ISecurityProvider = DI.inject()
    // TODO: Set settings only if the settings got loaded (requirement: Settings file exists)
    private val settingsFlow: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings())
    private val publicKeyFlow: MutableStateFlow<IKey?> = MutableStateFlow(null)
    private val privateKeyFlow: MutableStateFlow<IKey?> = MutableStateFlow(null)
    private val fileSystem = FileSystem.SYSTEM
    private val settingsUpdateMutex = Mutex()
    private val cryptoProvider = securityProvider.getAsymmetricCryptoProvider()
    private val pubkeyFile = pathProvider(PUBKEY_FILE)
    private val configFile = pathProvider(CONFIG_FILE)
    private val signatureFile = pathProvider(SIGNATURE_FILE)

    init {
        // Await public key if settings exists and decrypt them
        if (fileSystem.exists(configFile) && fileSystem.exists(signatureFile)) {
            defaultCoroutineScope.launch {
                while (publicKeyFlow.value == null);
                fileSystem.read(signatureFile) {
                    val signature = readByteArray()
                    fileSystem.read(configFile) {
                        val config = readByteArray()
                        val valid = cryptoProvider.verifySignature(publicKeyFlow.value!!, signature,
                            config)
                        if (valid) {
                            settingsFlow.emit(Json.decodeFromString(config.decodeToString()))
                        } else {
                            // TODO: Inform user about invalid signature and hold application
                        }
                        close()
                    }
                    close()
                }
            }
        }

        // Load public key from file if exists
        if (fileSystem.exists(pubkeyFile)) {
            securityProvider.readKeyFromFile(pubkeyFile, ISecurityProvider.EnumAlgorithm.RSA)
                .collectAsync {
                    publicKeyFlow.emit(it)
                }
        }

        // Update private key and update public key if public key doesn't need to be created before
        securityProvider.wasAuthenticated().collectAsync { isAuthenticated ->
            if (isAuthenticated) {
                if (publicKeyFlow.value == null) {
                    cryptoProvider.getOrCreatePublicKey(KEY_NAME).collectAsync { publicKey ->
                        fileSystem.write(pubkeyFile) {
                            write(publicKey.raw())
                            flush()
                            close()
                        }
                        publicKeyFlow.emit(publicKey)
                    }
                }
                cryptoProvider.getOrCreatePrivateKey(KEY_NAME).collectAsync { privateKey ->
                    privateKeyFlow.emit(privateKey)
                }
            }
        }
    }

    fun update(updater: (AppSettings) -> AppSettings) {
        defaultCoroutineScope.launch {
            settingsUpdateMutex.withLock(this) {
                val settings = updater(settingsFlow.value)
                settingsFlow.emit(settings)

                // Await not-null private key, sign settings with RSA
                while (privateKeyFlow.value == null);
                // Write new signature info file and config itself
                val json = Json.encodeToString(AppSettings.serializer(), settings)
                cryptoProvider.createSignature(privateKeyFlow.value!!, json.encodeToByteArray())
                    .collect { signature ->
                        fileSystem.write(signatureFile) {
                            write(signature)
                            flush()
                            close()
                        }

                        fileSystem.write(configFile) {
                            write(json.encodeToByteArray())
                            flush()
                            close()
                        }
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
        private const val KEY_NAME = "app_preferences_lock"
        private val PUBKEY_FILE = "application_preferences.pubkey".toPath()
        private val CONFIG_FILE = "application_preferences.json".toPath()
        private val SIGNATURE_FILE = "application_preferences.signature".toPath()
    }
}