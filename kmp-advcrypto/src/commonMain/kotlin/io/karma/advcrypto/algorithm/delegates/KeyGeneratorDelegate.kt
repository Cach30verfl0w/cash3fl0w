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

package io.karma.advcrypto.algorithm.delegates

import io.karma.advcrypto.AbstractProvider
import io.karma.advcrypto.algorithm.specs.KeyGeneratorSpec
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.KeyPair
import io.karma.advcrypto.wrapper.KeyGenerator
import io.karma.advcrypto.wrapper.KeyPairGenerator


data class KeyGenContext<C>(val generatorSpec: KeyGeneratorSpec, val internalContext: C)

/**
 * This class is used to create a key generator for a specific algorithm. The created key generator
 * factory and information are stored in [AbstractProvider] in the algorithm. You can configure the
 * following properties of the key generator:
 * - [KeyGeneratorDelegate.keyPurposes] (must be set): The allowed purposes of the generated key
 * - [KeyGeneratorDelegate.defaultKeySize] (must be set): The key size if it's is not in the spec
 * - [KeyGeneratorDelegate.allowedKeySizes] (must be set): The key sizes supported by the algorithm
 * - [KeyGeneratorDelegate.keyPairGenerator] (optionally): The key pair generator closure
 * - [KeyGeneratorDelegate.keyGenerator] (optionally): The key generator closure
 *
 * @author Cedric Hammes
 * @since  11/06/2024
 */
class KeyGeneratorDelegate<C: Any>(
    val keyPurposes: UByte,
    val defaultKeySize: Int,
    val allowedKeySizes: Array<Int>
) {
    lateinit var initializer: ((KeyGeneratorSpec) -> KeyGenContext<C>)
        private set
    private var keyPairGenerator: ((context: KeyGenContext<C>) -> KeyPair)? = null
    private var keyGenerator: ((context: KeyGenContext<C>) -> Key)? = null
    private var close: ((context: KeyGenContext<C>) -> Unit)? = null

    /**
     * This method creates an instance of a public-private keypair generator if a key pair generator
     * was registered by the provider. This is used in the cross-platform part of the API to acquire
     * a keypair generator for the user.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun createKeyPairGenerator(): KeyPairGenerator {
        if (keyPairGenerator == null) {
            throw UnsupportedOperationException(
                "Unable to create keypair generator without keypair generator specified"
            )
        }

        return object: KeyPairGenerator {
            private var context: KeyGenContext<C>? = null

            override fun initialize(spec: KeyGeneratorSpec): KeyPairGenerator {
                context = initializer(spec)
                return this
            }

            override fun generateKeyPair(): KeyPair {
                return keyPairGenerator!!.invoke(context!!)
            }

            override fun close() {
                if (close != null) {
                    close()
                }
            }
        }
    }

    /**
     * This method creates an instance of a key generator if a key pair generator was registered by
     * the provider. This is used in the cross-platform part of the API to acquire a key generator
     * for the user.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun createKeyGenerator(): KeyGenerator {
        if (keyGenerator == null) {
            throw UnsupportedOperationException(
                "Unable to create key generator without key generator specified"
            )
        }

        return object: KeyGenerator {
            private var context: KeyGenContext<C>? = null

            override fun initialize(spec: KeyGeneratorSpec): KeyGenerator {
                context = initializer(spec)
                return this
            }

            override fun generateKey(): Key {
                return keyGenerator!!.invoke(context!!)
            }

            override fun close() {
                if (close != null) {
                    close()
                }
            }
        }
    }

    /**
     * This method is used as a delegate for the creation of a internal context for the key
     * generator. This internal context can be the original key generator from Android etc.
     * and can used in the keypair or key generation closure.
     *
     * @param closure                The constructor of the generator's "internal" context
     * @throws IllegalStateException Thrown if this function is called twice
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun initializer(closure: (KeyGeneratorSpec) -> C) {
        this.initializer = { keyGenSpec ->
            if ((keyGenSpec.purposes and this.keyPurposes) != keyGenSpec.purposes) {
                throw IllegalArgumentException("Key purposes ${keyGenSpec.purposes} not supported")
            }
            KeyGenContext(keyGenSpec, closure(keyGenSpec))
        }
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
    fun generateKeyPair(closure: (context: KeyGenContext<C>) -> KeyPair) {
        if (keyGenerator != null) {
            throw IllegalStateException(
                "Unable to register key pair generator after registering key generator"
            )
        }
        if (keyPairGenerator != null) {
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
    fun generateKey(closure: (context: KeyGenContext<C>) -> Key) {
        if (keyPairGenerator != null) {
            throw IllegalStateException(
                "Unable to register key generator after registering key pair generator"
            )
        }
        if (keyGenerator != null) {
            throw IllegalStateException("Keypair generator was already created")
        }
        this.keyGenerator = closure
    }

    /**
     * This method sets a delegate to the close function of the key/keypair generato.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun close(closure: (context: KeyGenContext<C>) -> Unit) {
        this.close = closure
    }
}
