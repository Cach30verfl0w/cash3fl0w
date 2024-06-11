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

package io.karma.advcrypto.android.providers

import io.karma.advcrypto.AbstractProvider
import io.karma.advcrypto.android.keys.AndroidKey
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.KeyPair
import java.security.KeyPairGenerator

class RSACryptoProvider: AbstractProvider(
    "RSA",
    "This class provides access to the RSA cryptosystem",
    "1.0.0-Dev"
) {
    override fun initialize() {
        algorithm("RSA") {
            keyGenerator<KeyPairGenerator>(
                Key.PURPOSE_ALL,
                arrayOf(1024, 2048, 4096, 8192)
            ) {
                initializer {
                    KeyPairGenerator.getInstance("RSA").apply {
                        /*initialize(KeyGenParameterSpec.Builder("").run {
                            build()
                        })*/
                    }
                }

                generateKeyPair {
                    val keyPair = it.generateKeyPair()
                    KeyPair(
                        AndroidKey(keyPair.public, 0), // TODO: Correct purpose
                        AndroidKey(keyPair.private, 1) // TODO: Correct purpose
                    )
                }
            }
        }
    }
}