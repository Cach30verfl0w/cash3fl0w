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

import io.karma.advcrypto.keys.formats.KeyFormat
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import libssl.BIO
import libssl.BIO_new
import libssl.BIO_s_secmem
import libssl.BIO_write
import libssl.PEM_read_bio_PrivateKey

// TODO: Detect key format, key size and algorithm (etc)

@OptIn(ExperimentalForeignApi::class)
object KeyDetectionUtil {

    private fun createSecureMemoryBuffer(pointer: CPointer<ByteVar>, size: Int): CPointer<BIO> =
        BIO_new(BIO_s_secmem()).apply {
            BIO_write(this, pointer, size)
        }?: throw RuntimeException("Error while writing key into secure memory BIO")

    private fun tryParseAsPEM(pointer: CPointer<ByteVar>, size: Int): Unit? {
        val privateKeyBuffer = createSecureMemoryBuffer(pointer, size)
        val privateKey = PEM_read_bio_PrivateKey(privateKeyBuffer, null, null, null)
        if (privateKey == null) {
            val publicKeyBuffer = createSecureMemoryBuffer(pointer, size)
            // TODO: Parse as public key and handle
        }


        return null
    }

    /*private fun tryParseAsPEM(pointer: CPointer<ByteVar>, size: Int): KeyInfo {
        val privateKeyBuffer = createSecureMemoryBuffer(pointer, size)
        val parsedPrivateKey = PEM_read_bio_PrivateKey(privateKeyBuffer, null, null, null)
        val algorithm = when (EVP_PKEY_get_base_id(parsedPrivateKey)) {
            EVP_PKEY_RSA -> "RSA"
            EVP_PKEY_ED25519 -> "ED25519"
            EVP_PKEY_EC -> {

            }
            else -> "Unknown"
        }

        BIO_free(privateKeyBuffer)
        TODO("Return key info")
    }*/

    fun detectKeyFormat(pointer: CPointer<ByteVar>, size: Int): KeyFormat? {
        /*val tempKeyBuffer = BIO_new_mem_buf(pointer, size)
       // val publicKey = PEM_read_bio_RSAPublicKey(tempKeyBuffer, null, null, null)
        val privateKey = PEM_read_bio_PrivateKey(tempKeyBuffer, null, null, null)
        if (privateKey != null) {
            println(OBJ_nid2sn(EVP_PKEY_get_base_id(privateKey))!!.toKString())
            EVP_PKEY_free(privateKey)
            return KeyFormat.PEM
        }*/
        return null
    }

}