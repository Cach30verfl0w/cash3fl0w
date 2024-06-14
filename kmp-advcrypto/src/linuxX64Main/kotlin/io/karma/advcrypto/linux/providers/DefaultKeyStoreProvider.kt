/*
 * Copyright (c) 2024 Cach30verfl0w
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

package io.karma.advcrypto.linux.providers

import io.karma.advcrypto.AbstractProvider
import io.karma.advcrypto.Providers
import io.karma.advcrypto.linux.utils.KeyReaderHelper
import io.karma.advcrypto.linux.utils.SecureHeap
import okio.FileSystem

class DefaultKeyStoreProvider: AbstractProvider(
    "Default KeyStore",
    "This provider provides access to the keystore interface on Linux devices",
    "1.0.0-Dev"
) {
    private val secHeap = SecureHeap(UShort.MAX_VALUE.toULong() + 1u, 0u)

    override fun initialize(providers: Providers) {
        keyStore("Default") {
            initialize {
                "Placeholder"
            }
            readKeyFromFile { _, path, algorithm, purposes ->
                FileSystem.SYSTEM.read(path) {
                    val data = readByteArray()
                    close()
                    return@readKeyFromFile KeyReaderHelper.tryParse(data, purposes, algorithm,
                        secHeap)?: throw RuntimeException("Unable to parse key, no valid format!")

                }
            }
        }
    }

    override fun close() {
        this.secHeap.close()
    }
}