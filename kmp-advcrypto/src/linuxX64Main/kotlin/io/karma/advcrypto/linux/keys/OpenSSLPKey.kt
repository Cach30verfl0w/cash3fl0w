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
import io.karma.advcrypto.keys.enum.KeyFormat
import io.karma.advcrypto.keys.enum.KeyType
import io.karma.advcrypto.store.BIOMemoryBuffer
import io.karma.advcrypto.store.MemoryBuffer
import io.karma.advcrypto.store.emptyBuffer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import libssl.EVP_PKEY
import libssl.EVP_PKEY_ED25519
import libssl.EVP_PKEY_RSA
import libssl.EVP_PKEY_free
import libssl.EVP_PKEY_get_base_id
import libssl.OBJ_nid2sn
import libssl.PEM_write_bio_PUBKEY
import libssl.i2d_PUBKEY_bio
import libssl.i2d_PrivateKey_bio

@OptIn(ExperimentalForeignApi::class)
class OpenSSLPKey(val rawKey: CPointer<EVP_PKEY>,
                  override val purposes: UByte, override val type: KeyType,
                  override val format: KeyFormat = KeyFormat.DER): Key {
    override fun copyEncodedInto(buffer: MemoryBuffer) {
        (emptyBuffer() as BIOMemoryBuffer).apply {
            when(format) {
                KeyFormat.DER -> when(type) {
                    KeyType.PUBLIC -> i2d_PUBKEY_bio(this.bio, rawKey)
                    KeyType.PRIVATE -> i2d_PrivateKey_bio(this.bio, rawKey)
                    else -> throw UnsupportedOperationException("Unsupported key type")
                }
                KeyFormat.PEM -> PEM_write_bio_PUBKEY(this.bio, rawKey)
                else -> throw UnsupportedOperationException("Format '${format}' supported")
            }
        }.copyInto(buffer)
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