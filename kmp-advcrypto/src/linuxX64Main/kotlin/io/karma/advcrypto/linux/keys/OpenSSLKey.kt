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

import io.karma.advcrypto.annotations.InsecureCryptoApi
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.enum.KeyFormat
import io.karma.advcrypto.keys.enum.KeyType
import io.karma.advcrypto.linux.utils.SecureHeap
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import libssl.ERR_error_string
import libssl.ERR_get_error
import libssl.RAND_bytes

@OptIn(ExperimentalForeignApi::class)
class OpenSSLKey(private val secureHeap: SecureHeap,
                 override val purposes: UByte,
                 override val algorithm: String,
                 private val rawDataPtr: CPointer<UByteVar>,
                 private val rawDataSize: ULong,
                 override val type: KeyType
): Key {
    @InsecureCryptoApi
    override val encoded: ByteArray = ByteArray(rawDataSize.toInt()) { rawDataPtr[it].toByte() }
    override val format: KeyFormat = KeyFormat.DER // TODO: Derive from key

    override fun close() {
        secureHeap.free(rawDataSize, rawDataPtr)
    }

    companion object {
        fun generateRandom(
            secureHeap: SecureHeap,
            keySize: Int,
            purposes: UByte,
            algorithm: String,
            type: KeyType
        ): OpenSSLKey {
            val dataSize = (keySize / 8).toULong()
            val rawDataPtr = secureHeap.allocate((keySize / 8).toULong()).reinterpret<UByteVar>()
            if (RAND_bytes(rawDataPtr, dataSize.toInt()) != 1) {
                throw Exception(ERR_error_string(ERR_get_error(), null)?.toKString())
            }

            return OpenSSLKey(secureHeap, purposes, algorithm, rawDataPtr, dataSize, type)
        }
    }


}
