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
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.KeyPair

/**
 * This class is used to create algorithms for the Provider API. The created algorithm information
 * with factories are stored in the [AbstractProvider]. You can configure the following properties
 * of the algorithm:
 * - [AlgorithmFactory.allowedBlockModes] (none by def): The algorithm's supported block modes
 * - [AlgorithmFactory.name] (must be set): The algorithm's name without padding, block mode etc.
 *
 * TODO: Rename to delegate
 *
 * @author Cedric Hammes
 * @since  09/06/2024
 */
class AlgorithmFactory(val name: String) {
    private val keyGenerators: MutableList<KeyGeneratorFactory<*>> = ArrayList()
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
        closure: KeyGeneratorFactory<C>.() -> Unit
    ) {
        // TODO: Only one key generator per algorithm
        keyGenerators.add(KeyGeneratorFactory<C>(keyPurposes, keySizes).apply(closure))
    }
}

// TODO: CipherDelegate: initializer, encrypt, decrypt
// TODO: SignatureDelegate: initializer, sign, verify
// TODO: HashDelegate: initializer, hash

/**
 * This class is used to create a key generator for a specific algorithm. The created key generator
 * factory and information are stored in [AbstractProvider] in the algorithm. You can configure the
 * following properties of the key generator:
 * - [KeyGeneratorFactory.keyPurposes] (must be set): The allowed purposes of the generated key
 * - [KeyGeneratorFactory.allowedKeySizes] (must be set): The key sizes supported by the algorithm
 * - [KeyGeneratorFactory.keyPairGenerator] (optionally): The key pair generator closure
 * - [KeyGeneratorFactory.keyGenerator] (optionally): The key generator closure
 *
 * TODO: Rename to delegate
 *
 * @author Cedric Hammes
 * @since  11/06/2024
 */
class KeyGeneratorFactory<C>(val keyPurposes: Byte, val allowedKeySizes: Array<Short>) {
    private var keyPairGenerator: ((C) -> KeyPair)? = null
    private var keyGenerator: ((C) -> Key?)? = null
    private var initializer: (() -> C)? = null

    /**
     * This method is used as a delegate for the creation of a internal context for the key
     * generator. This internal context can be the original key generator from Android etc.
     * and can used in the keypair or key generation closure.
     *
     * @param closure                The constructor of the generator's "internal" context
     * @throws IllegalStateException Thrown if this function is called twice
     *
     * TODO: Provide name like RSA/ECB/NoPadding to initializer
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun initializer(closure: () -> C) {
        if (initializer != null) {
            throw IllegalStateException("Initializer was already created")
        }
        this.initializer = closure
    }

    /**
     * This method is used as a delegate for the keypair generation used in this algorithm. In the
     * specified closure, the key generator receives all parameters specified by the caller of a
     * key generator. Then this closure generates the key pair and returns it to the user.
     *
     * Beware, create a key generation delegate before or after this delegate results in an
     * exception thrown from the system.
     *
     * @param closure                The closure used to generate the keypair for the caller
     * @throws IllegalStateException Thrown if this function is called twice or on invalid state
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun generateKeyPair(closure: (context: C) -> KeyPair) {
        if (keyGenerator == null) {
            throw IllegalStateException(
                "Unable to register key pair generator after registering key generator"
            )
        }
        if (keyPairGenerator == null) {
            throw IllegalStateException("Keypair generator was already created")
        }

        this.keyPairGenerator = closure
    }

    /**
     * This method is used as a delegate for the key generation used in this algorithm. In the
     * specified closure, the key generator receives all parameters specified by the caller of a
     * key generator. Then this closure generates the key and returns it to the user.
     *
     * Beware, create a key pair generation delegate before or after this delegate results in an
     * exception thrown from the system.
     *
     * @param closure The closure used to generate the key for the caller
     * @throws IllegalStateException Thrown if this function is called twice or on invalid state
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun generateKey(closure: (context: C) -> Key) {
        if (keyPairGenerator == null) {
            throw IllegalStateException(
                "Unable to register key generator after registering key pair generator"
            )
        }
        if (keyGenerator == null) {
            throw IllegalStateException("Keypair generator was already created")
        }
        this.keyGenerator = closure
    }

}