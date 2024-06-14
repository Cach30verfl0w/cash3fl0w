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

package io.karma.advcrypto.linux.providers

import io.karma.advcrypto.AbstractProvider
import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.delegates.KeyGenContext
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.KeyPair
import io.karma.advcrypto.keys.enum.KeyType
import io.karma.advcrypto.linux.keys.OpenSSLKey
import io.karma.advcrypto.linux.keys.OpenSSLPKey
import io.karma.advcrypto.linux.utils.SecureHeap
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.refTo
import libssl.BN_free
import libssl.BN_new
import libssl.BN_set_word
import libssl.EVP_PKEY_free
import libssl.EVP_PKEY_new
import libssl.EVP_PKEY_set1_RSA
import libssl.MD5
import libssl.RSAPublicKey_dup
import libssl.RSA_F4
import libssl.RSA_free
import libssl.RSA_generate_key_ex
import libssl.RSA_new
import libssl.SHA1
import libssl.SHA224
import libssl.SHA224_DIGEST_LENGTH
import libssl.SHA256
import libssl.SHA256_DIGEST_LENGTH
import libssl.SHA384
import libssl.SHA384_DIGEST_LENGTH
import libssl.SHA512
import libssl.SHA512_DIGEST_LENGTH

class OpenSSLCryptoProvider: AbstractProvider(
    "Default",
    "This class provides access to the default asymmetric and symmetric algorithms on Linux",
    "1.0.0-Dev"
) {
    private val secureHeap = SecureHeap(UShort.MAX_VALUE.toULong() + 1u, 0u)

    @OptIn(ExperimentalForeignApi::class, ExperimentalStdlibApi::class)
    override fun initialize(providers: Providers) {
        fun openSSLHasher(name: String, size: Int,
                          hasher: (CValuesRef<UByteVar>, ULong, CValuesRef<UByteVar>) -> Unit) {
            algorithm(providers, name) {
                hasher {
                    initialize { 0 }
                    hash { _, data ->
                        val output = UByteArray(size)
                        hasher(data.toUByteArray().refTo(0), data.size.toULong(), output.refTo(0))
                        output.toByteArray().toHexString()
                    }
                }
            }
        }

        openSSLHasher("MD5", 16, ::MD5)
        openSSLHasher("SHA1", 20, ::SHA1)
        openSSLHasher("SHA224", SHA224_DIGEST_LENGTH, ::SHA224)
        openSSLHasher("SHA256", SHA256_DIGEST_LENGTH, ::SHA256)
        openSSLHasher("SHA384", SHA384_DIGEST_LENGTH, ::SHA384)
        openSSLHasher("SHA512", SHA512_DIGEST_LENGTH, ::SHA512)

        algorithm(providers, "AES") {
            keyGenerator<Unit>(Key.PURPOSES_SYMMETRIC, arrayOf(128, 196, 256), 256) {
                initializer { spec -> KeyGenContext(spec, Unit) }
                generateKey { context ->
                    OpenSSLKey.generateRandom(
                        secureHeap,
                        context.generatorSpec.keySize?: defaultKeySize,
                        context.generatorSpec.purposes,
                        "AES",
                        KeyType.SECRET
                    )
                }
            }
        }

        algorithm(providers, "RSA") {
            keyGenerator(Key.PURPOSES_ALL, arrayOf(1024, 2048, 4096), 4096) {
                initializer {
                    val bne = BN_new()
                    if (BN_set_word(bne, RSA_F4.toULong()) != 1) {
                        BN_free(bne)
                        throw RuntimeException("Initialization of RSA key generator failed")
                    }
                    bne!!
                }

                generateKeyPair { context ->
                    val keySize = context.generatorSpec.keySize?: defaultKeySize
                    val purposes = context.generatorSpec.purposes

                    // Generate keys
                    val rsa = RSA_new()
                    if (RSA_generate_key_ex(rsa, keySize, context.internalContext, null) != 1) {
                        RSA_free(rsa)
                        throw RuntimeException("RSA key generation failed")
                    }

                    val privateKey = EVP_PKEY_new()
                    if (EVP_PKEY_set1_RSA(privateKey, rsa) != 1) {
                        EVP_PKEY_free(privateKey)
                        RSA_free(rsa)
                        throw RuntimeException("Unable to acquire private key from RSA generator")
                    }

                    val publicKey = EVP_PKEY_new()
                    val publicKeyRSA = RSAPublicKey_dup(rsa)
                    if (EVP_PKEY_set1_RSA(publicKey, publicKeyRSA) != 1) {
                        EVP_PKEY_free(privateKey)
                        EVP_PKEY_free(publicKey)
                        RSA_free(publicKeyRSA)
                        RSA_free(rsa)
                        throw RuntimeException("Unable to acquire public key from RSA generator")
                    }

                    // Return key pair
                    KeyPair(
                        OpenSSLPKey(
                            publicKey!!,
                            (purposes and (Key.PURPOSE_ENCRYPT or Key.PURPOSE_VERIFY)),
                            KeyType.PUBLIC
                        ),
                        OpenSSLPKey(
                            privateKey!!,
                            (purposes and (Key.PURPOSE_DECRYPT or Key.PURPOSE_SIGNING)),
                            KeyType.PRIVATE
                        )
                    )
                }

                close { context ->
                    BN_free(context.internalContext)
                }
            }
        }
    }

    override fun close() {
        this.secureHeap.close()
    }
}