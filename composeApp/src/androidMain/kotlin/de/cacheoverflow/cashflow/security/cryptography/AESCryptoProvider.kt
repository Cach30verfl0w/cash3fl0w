/*
 * Copyright 2024 Cach30verfl0w
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

package de.cacheoverflow.cashflow.security.cryptography

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import de.cacheoverflow.cashflow.security.AndroidKey
import de.cacheoverflow.cashflow.security.AndroidSecurityProvider
import de.cacheoverflow.cashflow.security.IKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

class AESCryptoProvider(
    private val securityProvider: AndroidSecurityProvider,
    private val usePadding: Boolean = true
): ISymmetricCryptoProvider {
    override fun encrypt(key: IKey, message: ByteArray): Flow<ByteArray> = flow {
        val cipher = Cipher.getInstance(this@AESCryptoProvider.getAlgorithm())
        cipher.init(Cipher.ENCRYPT_MODE, (key as AndroidKey).rawKey)
        val encryptedMessage = cipher.doFinal(message)

        // Concatenate encrypted ciphertext with IV size and IV itself
        val returnValue = ByteArray(cipher.iv.size + encryptedMessage.size + Int.SIZE_BYTES)
        val ivSize = ByteBuffer.allocate(Int.SIZE_BYTES).order(ByteOrder.nativeOrder())
            .putInt(cipher.iv.size).array()
        System.arraycopy(ivSize, 0, returnValue, 0, Int.SIZE_BYTES)
        System.arraycopy(cipher.iv, 0, returnValue, Int.SIZE_BYTES, cipher.iv.size)
        System.arraycopy(encryptedMessage, 0, returnValue, cipher.iv.size + Int.SIZE_BYTES,
            encryptedMessage.size)

        // Emit
        emit(returnValue)
    }

    override fun decrypt(key: IKey, message: ByteArray): Flow<ByteArray> = flow {
        // Extract IV size and IV
        val initVector = ByteArray(ByteBuffer.wrap(message).order(ByteOrder.nativeOrder()).getInt())
        System.arraycopy(message, Int.SIZE_BYTES, initVector, 0, initVector.size)

        val encrypted = ByteArray(message.size - initVector.size - Int.SIZE_BYTES)
        System.arraycopy(message, initVector.size + Int.SIZE_BYTES, encrypted, 0, encrypted.size)

        // Decrypt
        val cipher = Cipher.getInstance(this@AESCryptoProvider.getAlgorithm())
        cipher.init(Cipher.DECRYPT_MODE, (key as AndroidKey).rawKey, if (usePadding) {
            IvParameterSpec(initVector)
        } else {
            GCMParameterSpec(128, initVector)
        })
        emit(cipher.doFinal(encrypted))
    }

    override fun generateKey(alias: String, requireAuth: Boolean): Flow<IKey> = flow {
        val authPossible = securityProvider.isBiometricAuthenticationAvailable() && requireAuth
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
            AndroidSecurityProvider.KEY_STORE)
        val purpose = KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT
        keyGenerator.init(KeyGenParameterSpec.Builder(alias, purpose).run {
            setUserAuthenticationRequired(authPossible)
            // TODO: Change
            setUserAuthenticationParameters(1000000, KeyProperties.AUTH_BIOMETRIC_STRONG)
            setKeySize(256)
            if (usePadding) {
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            } else {
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            }
            build()
        })
        emit(AndroidKey(keyGenerator.generateKey(), usePadding))
    }

    override fun getAlgorithm(): String = "AES/${if (usePadding) {
        "${KeyProperties.BLOCK_MODE_CBC}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}"
    } else {
        "${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}"
    }}"
    override fun getName(): String = "AES"
}