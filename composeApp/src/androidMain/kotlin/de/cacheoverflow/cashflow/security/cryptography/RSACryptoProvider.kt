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
import de.cacheoverflow.cashflow.security.KeyPair
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.KeyPairGenerator
import javax.crypto.Cipher

class RSACryptoProvider(
    private val securityProvider: AndroidSecurityProvider,
    private val usePadding: Boolean = true
): IAsymmetricCryptoProvider {

    override fun encrypt(key: IKey, message: ByteArray): Flow<ByteArray> = flow {
        val cipher = Cipher.getInstance(this@RSACryptoProvider.getAlgorithm())
        cipher.init(Cipher.ENCRYPT_MODE, (key as AndroidKey).rawKey)
        emit(cipher.doFinal(message))
    }

    override fun decrypt(key: IKey, message: ByteArray): Flow<ByteArray> = flow {
        val cipher = Cipher.getInstance(this@RSACryptoProvider.getAlgorithm())
        cipher.init(Cipher.DECRYPT_MODE, (key as AndroidKey).rawKey)
        emit(cipher.doFinal(message))
    }

    override fun generateKeyPair(alias: String, requireAuth: Boolean): Flow<KeyPair> = flow {
        val authPossible = securityProvider.isBiometricAuthenticationAvailable() && requireAuth
        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA,
            AndroidSecurityProvider.KEY_STORE)
        val purpose = KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT
        keyPairGenerator.initialize(KeyGenParameterSpec.Builder(alias, purpose).run {
            setUserAuthenticationRequired(authPossible)
            setKeySize(4096)
            if (usePadding) {
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                setDigests(KeyProperties.DIGEST_SHA256)
                setBlockModes(KeyProperties.BLOCK_MODE_ECB)
            } else {
                throw UnsupportedOperationException("RSA without padding isn't supported")
            }
            build()
        })
        val keyPair = keyPairGenerator.generateKeyPair()
        emit(KeyPair(
            AndroidKey(keyPair.public, true),
            AndroidKey(keyPair.private, true)
        ))
    }

    override fun getAlgorithm(): String = "RSA/${KeyProperties.BLOCK_MODE_ECB}/${KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1}"

    override fun getName(): String = "RSA"

}