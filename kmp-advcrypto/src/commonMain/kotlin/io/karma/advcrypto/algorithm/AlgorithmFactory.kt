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
 * - [AlgorithmFactory.allowedKeySizes] (must be set): The key sizes supported by the algorithm
 * - [AlgorithmFactory.allowedBlockModes] (none by default): The algorithm's supported block modes
 * - [AlgorithmFactory.keyPurposes] (must be set): The allowed purposes of the generated key
 * - [AlgorithmFactory.name] (must be set): The algorithm's name without padding, block mode etc.
 *
 *
 * @author Cedric Hammes
 * @since  09/06/2024
 */
class AlgorithmFactory(val name: String, val keyPurposes: Byte) {
    var allowedKeySizes: Array<Short>? = null
    var allowedBlockModes: Byte = 0

    /**
     * This method is used to generate a key generator definition for the algorithm. This function
     * should be called one-time by the developer because otherwise an exception will be thrown.
     *
     * @param factory The factory function for the key generator
     *
     * @author Cedric Hammes
     * @since  09/06/2024
     */
    fun keyGenerator(factory: KeyGeneratorFactory.() -> Unit) {
        KeyGeneratorFactory().apply(factory) // TODO: Do something
    }

    /**
     * This class is used to specify a key generator for the algorithm creating. A key generator
     * is used in encryption and signature algorithms like AES, CRYSTALS-Kyber or RSA.
     *
     * @author Cedric Hammes
     * @since  09/06/2024
     */
    class KeyGeneratorFactory {

        /**
         * This method registers a factory for an instance of the key generator. This is used when
         * the developer creates a key generator with this API.
         *
         * @author Cedric Hammes
         * @since  09/06/2024
         */
        fun factory(factory: () -> Unit) {

        }

    }
}