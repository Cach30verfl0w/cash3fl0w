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
 * - [AlgorithmFactory.allowedBlockModes] (none by def): The algorithm's supported block modes
 * - [AlgorithmFactory.name] (must be set): The algorithm's name without padding, block mode etc.
 *
 * @author Cedric Hammes
 * @since  09/06/2024
 */
class AlgorithmFactory(val name: String) {
    var allowedBlockModes: Byte = 0

    /**
     * This method is used to generate a new key generator for the algorithm. If a key generator was
     * set before this call, the factory returns an exception.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun keyGenerator(
        keyPurposes: Byte,
        keySizes: Array<Short>,
        closure: KeyGeneratorFactory.() -> Unit
    ) {
        KeyGeneratorFactory(keyPurposes, keySizes).apply(closure)
    }
}

/**
 * This class is used to create a key generator for a specific algorithm. The created key generator
 * factory and information are stored in [AbstractProvider] in the algorithm. You can configure the
 * following properties of the key generator:
 * - [KeyGeneratorFactory.keyPurposes] (must be set): The allowed purposes of the generated key
 * - [KeyGeneratorFactory.allowedKeySizes] (must be set): The key sizes supported by the algorithm
 *
 * @author Cedric Hammes
 * @since  11/06/2024
 */
class KeyGeneratorFactory(val keyPurposes: Byte, val allowedKeySizes: Array<Short>) {
}