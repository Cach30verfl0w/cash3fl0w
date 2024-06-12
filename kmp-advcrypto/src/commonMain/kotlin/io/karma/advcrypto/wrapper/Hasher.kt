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

@OptIn(ExperimentalStdlibApi::class)
interface Hasher: AutoCloseable {

    fun hash(data: ByteArray): String

    companion object {
        fun getInstance(algorithm: String): Hasher {
            return Providers.getAlgorithmByName(algorithm)?.hasher?.createHasher()?: throw
            UnsupportedOperationException("The algorithm $algorithm doesn't exists or this algorithm doesn't support key generation")
        }
    }
}