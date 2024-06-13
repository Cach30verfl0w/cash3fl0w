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
import io.karma.advcrypto.linux.keys.OpenSSLKey
import io.karma.advcrypto.linux.utils.SecureHeap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.reinterpret
import libssl.BIO_ctrl_pending
import libssl.BIO_free
import libssl.BIO_new
import libssl.BIO_read
import libssl.BIO_s_secmem
import libssl.BN_free
import libssl.BN_new
import libssl.BN_set_word
import libssl.PEM_write_bio_RSAPrivateKey
import libssl.PEM_write_bio_RSAPublicKey
import libssl.RSA_F4
import libssl.RSA_free
import libssl.RSA_generate_key_ex
import libssl.RSA_new

class OpenSSLCryptoProvider: AbstractProvider(
    "Default",
    "This class provides access to the default asymmetric and symmetric algorithms on Linux",
    "1.0.0-Dev"
) {
    private val secureHeap = SecureHeap(UShort.MAX_VALUE.toULong() + 1u, 0u)

    @OptIn(ExperimentalForeignApi::class)
    override fun initialize(providers: Providers) {
        algorithm(providers, "AES") {
            keyGenerator<Unit>(Key.PURPOSES_SYMMETRIC, arrayOf(128, 196, 256), 256) {
                initializer { spec -> KeyGenContext(spec, Unit) }
                generateKey { context ->
                    OpenSSLKey.generateRandom(
                        secureHeap,
                        context.generatorSpec.keySize?: defaultKeySize,
                        context.generatorSpec.purposes,
                        "AES"
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

                    // Generate keys
                    val rsa = RSA_new()
                    if (RSA_generate_key_ex(rsa, keySize, context.internalContext, null) != 1) {
                        RSA_free(rsa)
                        throw RuntimeException("RSA key generation failed")
                    }

                    // Write public and private key into memory
                    val privKeyBio = BIO_new(BIO_s_secmem())
                    if (PEM_write_bio_RSAPrivateKey(privKeyBio, rsa, null, null, 0, null, null) != 1) {
                        BIO_free(privKeyBio)
                        RSA_free(rsa)
                        throw RuntimeException("Writing private key into memory failed")
                    }

                    val pubKeyBio = BIO_new(BIO_s_secmem())
                    if (PEM_write_bio_RSAPublicKey(pubKeyBio, rsa) != 1) {
                        BIO_free(privKeyBio)
                        BIO_free(pubKeyBio)
                        RSA_free(rsa)
                        throw RuntimeException("Writing public key into memory failed")
                    }

                    // Read public key into other secure memory
                    val pubKeyBufferSize = BIO_ctrl_pending(pubKeyBio)
                    val pubKeyBuffer = secureHeap.allocate(pubKeyBufferSize)
                    val pubLen = BIO_read(pubKeyBio, pubKeyBuffer, pubKeyBufferSize.toInt())
                    if (pubLen < 0) {
                        secureHeap.free(pubKeyBufferSize, pubKeyBuffer)
                        BIO_free(privKeyBio)
                        BIO_free(pubKeyBio)
                        RSA_free(rsa)
                    }

                    // Read public key into other secure memory
                    val privKeyBufferSize = BIO_ctrl_pending(privKeyBio)
                    val privKeyBuffer = secureHeap.allocate(privKeyBufferSize)
                    val privLen = BIO_read(privKeyBio, privKeyBuffer, privKeyBufferSize.toInt())
                    if (privLen < 0) {
                        secureHeap.free(pubKeyBufferSize, pubKeyBuffer)
                        secureHeap.free(privKeyBufferSize, privKeyBuffer)
                        BIO_free(privKeyBio)
                        BIO_free(pubKeyBio)
                        RSA_free(rsa)
                    }

                    // Format

                    // Free BIO data
                    BIO_free(privKeyBio)
                    BIO_free(pubKeyBio)

                    // Return key pair
                    KeyPair(
                        OpenSSLKey(secureHeap, 0u, "RSA", pubKeyBuffer.reinterpret(),
                            pubKeyBufferSize),
                        OpenSSLKey(secureHeap, 0u, "RSA", privKeyBuffer.reinterpret(),
                            privKeyBufferSize)
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