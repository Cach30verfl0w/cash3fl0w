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

package io.karma.advcrypto.linux.utils

import io.karma.advcrypto.keys.Key
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import libssl.BIO
import libssl.BIO_free
import libssl.BIO_new
import libssl.BIO_s_secmem
import libssl.BIO_write
import libssl.EVP_PKEY
import libssl.PEM_read_bio_PUBKEY
import libssl.PEM_read_bio_PrivateKey

// TODO: Detect key format, key size and algorithm (etc)

@OptIn(ExperimentalForeignApi::class)
object KeyDetectionUtil {

    private fun createSecureMemoryBuffer(pointer: CPointer<ByteVar>, size: ULong): CPointer<BIO> =
        BIO_new(BIO_s_secmem()).apply {
            BIO_write(this, pointer, size.toInt())
        }?: throw RuntimeException("Error while writing key into secure memory BIO")

    private fun tryParseAsPEM(pointer: CPointer<ByteVar>, size: ULong): Key? {
        fun getPEMKey(pointer: CPointer<ByteVar>, size: ULong): Pair<CPointer<EVP_PKEY>, Boolean>? {
            val privateKeyBuffer = createSecureMemoryBuffer(pointer, size)
            val privateKey = PEM_read_bio_PrivateKey(privateKeyBuffer, null, null, null)
            BIO_free(privateKeyBuffer)
            if (privateKey != null) {
                return Pair(privateKey, true)
            }

            val publicKeyBuffer = createSecureMemoryBuffer(pointer, size)
            val publicKey = PEM_read_bio_PUBKEY(publicKeyBuffer, null, null, null)
            BIO_free(publicKeyBuffer)
            if (publicKey != null) {
                return Pair(publicKey, false)
            }
            return null
        }

        val pemParsedKey = getPEMKey(pointer, size)
        if (pemParsedKey != null) {

        }
        return null
    }

}