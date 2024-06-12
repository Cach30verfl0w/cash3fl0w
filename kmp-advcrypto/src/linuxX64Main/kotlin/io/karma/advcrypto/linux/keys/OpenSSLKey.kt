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

package io.karma.advcrypto.linux.keys

import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.linux.utils.SecureHeap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import libssl.ERR_func_error_string
import libssl.ERR_get_error
import libssl.RAND_bytes

@OptIn(ExperimentalForeignApi::class, ExperimentalStdlibApi::class)
class OpenSSLKey(private val secureHeap: SecureHeap, val keySize: Int,
                 override val purposes: UByte, override val algorithm: String): AutoCloseable, Key {
    private val rawDataPtr = secureHeap.allocate((keySize / 8).toULong()).reinterpret<UByteVar>()

    override fun close() {
        secureHeap.free((keySize / 8).toULong(), rawDataPtr)
    }

    companion object {
        fun generateRandom(secureHeap: SecureHeap, keySize: Int, purposes: UByte,
                           algorithm: String): OpenSSLKey =
            OpenSSLKey(secureHeap, keySize, purposes, algorithm).apply {
                if (RAND_bytes(rawDataPtr, 1) != 1) {
                    throw Exception(ERR_func_error_string(ERR_get_error())?.toKString())
                }
            }
    }


}
