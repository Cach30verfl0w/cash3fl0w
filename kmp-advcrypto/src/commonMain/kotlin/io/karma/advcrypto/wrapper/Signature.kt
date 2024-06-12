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
import io.karma.advcrypto.algorithm.delegates.SignatureDelegate
import io.karma.advcrypto.keys.Key

@OptIn(ExperimentalStdlibApi::class)
interface Signature: AutoCloseable {

    fun initVerify(key: Key)

    fun initSign(key: Key)

    fun sign(data: ByteArray): ByteArray

    fun verify(signature: ByteArray, original: ByteArray): Boolean

    companion object {
        /**
         * This method returns an instance of a signature, created by the internal architecture of
         * this library. This interface is implemented in [SignatureDelegate] and used here.
         *
         * @author Cedric Hammes
         * @since  11/06/2024
         */
        fun getInstance(algorithm: String): Signature {
            return Providers.getAlgorithmByName(algorithm)?.signature?.createSignature()?: throw
            UnsupportedOperationException("The algorithm $algorithm doesn't exists or this algorithm doesn't support signature")
        }
    }

}