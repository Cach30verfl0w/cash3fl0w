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

package io.karma.advcrypto.algorithm

import io.karma.advcrypto.AbstractProvider

/**
 * This class is used to create algorithms for the Provider API. The created algorithm information
 * with factories are stored in the [AbstractProvider]. You can configure the following properties
 * of the algorithm:
 * - [Algorithm.allowedBlockModes] (none by def): The algorithm's supported block modes
 * - [Algorithm.name] (must be set): The algorithm's name without padding, block mode etc.
 *
 * @author Cedric Hammes
 * @since  09/06/2024
 */
class Algorithm(val name: String) {
    var keyGenerator: KeyGeneratorDelegate<*>? = null
        private set
    var allowedBlockModes: Byte = 0

    /**
     * This method is used to generate a new key generator for the algorithm. If a key generator was
     * set before this call, the factory returns an exception.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun <C> keyGenerator(
        keyPurposes: Byte,
        keySizes: Array<Short>,
        closure: KeyGeneratorDelegate<C>.() -> Unit
    ) {
        if (keyGenerator != null) {
            throw IllegalStateException("You can set a key generator twice")
        }
        keyGenerator = KeyGeneratorDelegate<C>(keyPurposes, keySizes).apply(closure)
    }
}

// TODO: CipherDelegate: initializer, encrypt, decrypt
// TODO: SignatureDelegate: initializer, sign, verify
// TODO: HashDelegate: initializer, hash