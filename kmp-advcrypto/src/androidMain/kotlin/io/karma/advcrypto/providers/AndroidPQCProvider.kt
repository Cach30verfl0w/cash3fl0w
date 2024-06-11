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

package io.karma.advcrypto.providers

import io.karma.advcrypto.AbstractProvider
import io.karma.advcrypto.ExperimentalCryptoApi
import io.karma.advcrypto.keys.Key
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider
import java.security.KeyPairGenerator
import java.security.Security
import kotlin.experimental.or

/**
 * This is the Android-based provider for post-quantum cryptography (PQC) algorithms. All algorithms
 * registered are considered safe against quantum computer attacks. The research and development of
 * post-quantum algorithms is relatively new and therefore non-hybrid use is not recommended. This
 * API has also been marked as experimental for this reason.
 *
 * This provider supports the following algorithms:
 * - CRYSTALS-Kyber (KEM) 512, 768 and 1024; [CRYSTALS Homepage](https://pq-crystals.org/kyber)
 *
 * @author Cedric Hammes
 * @since  11/06/2024
 */
@ExperimentalCryptoApi
class AndroidPQCProvider: AbstractProvider(
    "Post-quantum Cryptography Provider",
    "This provider allows the usage of post-quantum crypto algorithms",
     "1.0.0-Experimental"
) {
    override fun initialize() {
        Security.addProvider(BouncyCastlePQCProvider())

        // CRYSTALS-Kyber is a post-quantum KEM (Key Encapsulation Mechanism) and one of the
        // finalists in the NIST post-quantum cryptography project.
        // See also: https://pq-crystals.org/kyber/
        algorithm("Kyber") {
            keyGenerator<KeyPairGenerator>(
                Key.PURPOSE_ENCRYPT or Key.PURPOSE_DECRYPT,
                arrayOf(512, 768, 1024),
            ) {
                initializer {
                    KeyPairGenerator.getInstance("Kyber")
                }
            }
        }
    }
}