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
import io.karma.advcrypto.algorithm.delegates.CipherDelegate
import io.karma.advcrypto.algorithm.delegates.HasherDelegate
import io.karma.advcrypto.algorithm.delegates.KeyGeneratorDelegate
import io.karma.advcrypto.algorithm.delegates.SignatureDelegate

/**
 * This class is used to create algorithms for the Provider API. The created algorithm information
 * with factories are stored in the [AbstractProvider]. You can configure the following properties
 * of the algorithm:
 * - [Algorithm.allowedBlockModes] (none by default): The algorithm's supported block modes
 * - [Algorithm.name] (must be set): The algorithm's name without padding, block mode etc.
 *
 * @author Cedric Hammes
 * @since  09/06/2024
 */
class Algorithm(val name: String) {
    var keyGenerator: KeyGeneratorDelegate<*>? = null
        private set
    var cipher: CipherDelegate<*>? = null
        private set
    var signature: SignatureDelegate<*>? = null
        private set
    var hasher: HasherDelegate<*>? = null
        private set
    
    var allowedBlockModes: Array<BlockMode> = arrayOf()
    var defaultBlockMode: BlockMode? = null

    /**
     * This method is used to generate a new key generator for the algorithm. If a key generator was
     * set before this call, the factory returns an exception.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun <C: Any> keyGenerator(
        keyPurposes: UByte,
        keySizes: Array<Int> = arrayOf(),
        defaultKeySize: Int = 0,
        closure: KeyGeneratorDelegate<C>.() -> Unit
    ) {
        if (keyGenerator != null) {
            throw IllegalStateException("You can set key generator twice")
        }
        keyGenerator = KeyGeneratorDelegate<C>(keyPurposes, defaultKeySize, keySizes).apply(closure)
    }

    /**
     * This method is used to generate a new signature for the algorithm. If a signature was set
     * before this class, the factory returns an exception.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun <C: Any> signature(closure: SignatureDelegate<C>.() -> Unit) {
        if (signature != null) {
            throw IllegalStateException("You can set signature twice")
        }
        signature = SignatureDelegate<C>().apply(closure)
    }

    /**
     * This method is used to generate a new cipher for the algorithm. If a cipher was set before
     * this call, the factory returns an exception.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun <C: Any> cipher(closure: CipherDelegate<C>.() -> Unit) {
        if (cipher != null) {
            throw IllegalStateException("You can set cipher twice")
        }
        cipher = CipherDelegate<C>(this).apply(closure)
    }

    /**
     * This method is used to generate a new hasher for the algorithm. If a hasher was set before
     * this call, the factory returns an exception.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun <C: Any> hasher(closure: HasherDelegate<C>.() -> Unit) {
        if (hasher != null) {
            throw IllegalStateException("You can set hasher twice")
        }
        hasher = HasherDelegate<C>().apply(closure)
    }
}
