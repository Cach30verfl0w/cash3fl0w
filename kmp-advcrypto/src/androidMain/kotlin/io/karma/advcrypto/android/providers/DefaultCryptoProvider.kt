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

package io.karma.advcrypto.android.providers

import android.security.keystore.KeyGenParameterSpec
import io.karma.advcrypto.AbstractProvider
import io.karma.advcrypto.algorithm.BlockMode
import io.karma.advcrypto.android.keys.AndroidKey
import io.karma.advcrypto.android.purposesToAndroid
import io.karma.advcrypto.annotations.InsecureCryptoApi
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.KeyPair
import java.security.KeyPairGenerator
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

class DefaultCryptoProvider: AbstractProvider(
    "Default",
    "This class provides access to the default asymmetric and symmetric algorithms",
    "1.0.0-Dev"
) {
    init {
        // As far as I know, only RSA with the ECB operation mode is supported. Therefore, ECB mode
        // is used here despite its vulnerability to statistical attacks.
        @OptIn(InsecureCryptoApi::class)
        algorithm("RSA") {
            allowedBlockModes = arrayOf(BlockMode.ECB)
            defaultBlockMode = BlockMode.ECB

            keyGenerator<KeyPairGenerator>(
                Key.PURPOSES_ALL,
                arrayOf(1024, 2048, 4096),
                4096
            ) {
                initializer { initSpec ->
                    val purposes = purposesToAndroid(initSpec.purposes)
                    val spec = KeyGenParameterSpec.Builder("AndroidKeyStore", purposes).run {
                        setKeySize(initSpec.keySize?: defaultKeySize)
                        setEncryptionPaddings(initSpec.padding.toString())
                        setBlockModes((initSpec.blockMode?: defaultBlockMode).toString())
                        // TODO: Auth
                        build()
                    }

                    KeyPairGenerator.getInstance("RSA").apply {
                        initialize(spec)
                    }
                }

                generateKeyPair { context ->
                    val purposes = context.generatorSpec.purposes
                    val keyPair = context.internalContext.generateKeyPair()
                    KeyPair(
                        AndroidKey(
                            keyPair.public,
                            purposes and (Key.PURPOSE_DECRYPT or Key.PURPOSE_SIGNING).inv()
                        ),
                        AndroidKey(
                            keyPair.private,
                            purposes and (Key.PURPOSE_ENCRYPT or Key.PURPOSE_VERIFY).inv()
                        )
                    )
                }
            }

            cipher<Cipher> {
                initializer { spec, _ ->
                    Cipher.getInstance("RSA/${spec.blockMode?: defaultBlockMode}/${spec.padding}")
                }

                encrypt { context, data ->
                    val cipher = context.internalContext
                    cipher.init(Cipher.ENCRYPT_MODE, (context.key as AndroidKey).raw)
                    cipher.doFinal(data)
                }
                decrypt { context, data ->
                    val cipher = context.internalContext
                    cipher.init(Cipher.DECRYPT_MODE, (context.key as AndroidKey).raw)
                    cipher.doFinal(data)
                }
            }
        }

        algorithm("AES") {
            allowedBlockModes = arrayOf(BlockMode.GCM, BlockMode.CBC)
            defaultBlockMode = BlockMode.GCM

            keyGenerator<KeyGenerator>(
                Key.PURPOSE_ENCRYPT or Key.PURPOSE_DECRYPT,
                arrayOf(128, 196, 256),
                256
            ) {
                initializer { initSpec ->
                    val purposes = purposesToAndroid(initSpec.purposes)
                    val spec = KeyGenParameterSpec.Builder("AndroidKeyStore", purposes).run {
                        setKeySize(initSpec.keySize?: defaultKeySize)
                        setEncryptionPaddings(initSpec.padding.toString())
                        setBlockModes((initSpec.blockMode?: defaultBlockMode).toString())
                        // TODO: Auth
                        build()
                    }

                    KeyGenerator.getInstance("AES").apply {
                        init(spec)
                    }
                }

                generateKey { context ->
                    AndroidKey(
                        context.internalContext.generateKey(),
                        context.generatorSpec.purposes
                    )
                }
            }
            cipher<Cipher> {
                initializer { spec, _ ->
                    Cipher.getInstance("AES/${spec.blockMode?: defaultBlockMode}/${spec.padding}")
                }
                encrypt { context, data ->
                    val cipher = context.internalContext
                    // TODO: Add support for non-default implemented key and support for padding etc
                    cipher.init(Cipher.ENCRYPT_MODE, (context.key as AndroidKey).raw)
                    val encryptedData = cipher.doFinal(data)
                    val initVector = cipher.iv
                    val finalData = ByteArray(encryptedData.size + initVector.size + 4)
                    val initVectorSize = ByteArray(Int.SIZE_BYTES)
                    initVectorSize[0] = ((initVector.size shr 24) and 0xFF).toByte()
                    initVectorSize[1] = ((initVector.size shr 16) and 0xFF).toByte()
                    initVectorSize[2] = ((initVector.size shr 8) and 0xFF).toByte()
                    initVectorSize[3] = ((initVector.size shr 0) and 0xFF).toByte()
                    System.arraycopy(initVectorSize, 0, finalData, 0, 4)
                    System.arraycopy(initVector, 0, finalData, 4, initVector.size)
                    System.arraycopy(encryptedData, 0, finalData, initVector.size + 4, encryptedData.size)
                    finalData
                }
                decrypt { context, data ->
                    val initVectorSize = (data[0].toInt() shl 24) or
                            (data[1].toInt() shl 16) or
                            (data[2].toInt() shl 8) or
                            (data[3].toInt() and 0xFF)
                    val initVector = ByteArray(initVectorSize)
                    System.arraycopy(data, 4, initVector, 0, initVector.size)

                    // Init cipher
                    val cipher = context.internalContext
                    // TODO: Add support for non-default implemented key and support for padding etc
                    cipher.init(Cipher.DECRYPT_MODE, (context.key as AndroidKey).raw,
                        when(context.spec.blockMode?: defaultBlockMode) {
                            BlockMode.GCM -> GCMParameterSpec(128, initVector)
                            else -> IvParameterSpec(initVector)
                        }
                    )

                    // Decrypt
                    val encryptedData = ByteArray(data.size - initVectorSize - 4)
                    System.arraycopy(data, initVectorSize + 4, encryptedData, 0, encryptedData.size)
                    cipher.doFinal(encryptedData)
                }
            }
        }
    }
}