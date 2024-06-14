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
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import libssl.BIO_ctrl_pending
import libssl.BIO_free
import libssl.BIO_new
import libssl.BIO_read
import libssl.BIO_s_mem
import libssl.EVP_PKEY
import libssl.EVP_PKEY_ED25519
import libssl.EVP_PKEY_RSA
import libssl.EVP_PKEY_free
import libssl.EVP_PKEY_get_base_id
import libssl.OBJ_nid2sn
import libssl.PEM_write_bio_PUBKEY
import libssl.i2d_PUBKEY_bio

@OptIn(ExperimentalForeignApi::class)
class OpenSSLPKey(private val rawKey: CPointer<EVP_PKEY>,
                  override val purposes: UByte, override val type: KeyType,
                  override val format: KeyFormat = KeyFormat.DER): Key {
    @InsecureCryptoApi
    override val encoded: ByteArray?
        get() {
            return when(this.format) {
                KeyFormat.DER -> {
                    val bio = BIO_new(BIO_s_mem())
                    i2d_PUBKEY_bio(bio, rawKey)
                    val data = ByteArray(BIO_ctrl_pending(bio).toInt())
                    BIO_read(bio, data.refTo(0), data.size)
                    BIO_free(bio)
                    data
                }
                KeyFormat.PEM -> {
                    val bio = BIO_new(BIO_s_mem())
                    PEM_write_bio_PUBKEY(bio, rawKey)
                    val data = ByteArray(BIO_ctrl_pending(bio).toInt())
                    BIO_read(bio, data.refTo(0), data.size)
                    BIO_free(bio)
                    data
                }
                else -> null
            }
        }

    override val algorithm: String = when(val baseId = EVP_PKEY_get_base_id(rawKey)) {
        EVP_PKEY_RSA -> "RSA"
        EVP_PKEY_ED25519 -> "ED25519"
        else -> throw UnsupportedOperationException(
            "Unsupported algorithm '${OBJ_nid2sn(baseId)!!.toKString()}'"
        )
    }

    override fun close() {
        EVP_PKEY_free(rawKey)
    }
}