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
import io.karma.advcrypto.algorithm.delegates.KeyGenContext
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.linux.keys.OpenSSLKey
import io.karma.advcrypto.linux.utils.SecureHeap

class OpenSSLCryptoProvider: AbstractProvider(
    "Default",
    "This class provides access to the default asymmetric and symmetric algorithms",
    "1.0.0-Dev"
) {
    private val secureHeap = SecureHeap(UShort.MAX_VALUE.toULong() + 1u, 0u)

    override fun initialize(providers: Providers) {
        algorithm(providers, "AES") {
            keyGenerator<Unit>(Key.PURPOSES_SYMMETRIC, arrayOf(128, 196, 256), 256) {
                initializer { spec -> KeyGenContext(spec, Unit) }
                generateKey { context ->
                    OpenSSLKey.generateRandom(
                        secureHeap,
                        context.generatorSpec.keySize?: defaultKeySize,
                        context.generatorSpec.purposes,
                        "AES"
                    )
                }
            }
        }
    }

    override fun close() {
        this.secureHeap.close()
    }
}