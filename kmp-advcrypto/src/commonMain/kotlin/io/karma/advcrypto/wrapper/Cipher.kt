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

package io.karma.advcrypto.wrapper

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.delegates.CipherDelegate
import io.karma.advcrypto.algorithm.specs.CipherSpec

/**
 * This interface represents the cross-platform implementation of a key generator, provided by this
 * library.
 *
 * @author Cedric Hammes
 * @since  11/06/2024
 */
@OptIn(ExperimentalStdlibApi::class)
interface Cipher: AutoCloseable {

    /**
     * This method initializes the cipher with the specified specification. This specification is
     * used while encryption/decryption.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun initialize(spec: CipherSpec)

    fun encrypt(data: ByteArray): ByteArray

    fun decrypt(data: ByteArray): ByteArray

    companion object {
        /**
         * This method returns an instance of a key generator created by the internal architecture
         * of this library. This interface is implemented in [CipherDelegate] and used here.
         *
         * @author Cedric Hammes
         * @since  11/06/2024
         */
        fun getInstance(providers: Providers, algorithm: String): Cipher {
            return providers.getAlgorithmByName(algorithm)?.cipher?.createCipher()?: throw
            UnsupportedOperationException("The algorithm $algorithm doesn't exists or this algorithm doesn't support ciphers")
        }
    }

}