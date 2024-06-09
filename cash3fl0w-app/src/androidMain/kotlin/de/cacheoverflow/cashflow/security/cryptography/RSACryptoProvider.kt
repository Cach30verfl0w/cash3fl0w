/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
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
import kotlinx.coroutines.flow.flowOf
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import javax.crypto.Cipher

class RSACryptoProvider(
    private val securityProvider: AndroidSecurityProvider,
    private val usePadding: Boolean = true
): IAsymmetricCryptoProvider {

    override fun getOrCreatePrivateKey(alias: String, requireAuth: Boolean): Flow<IKey> = flow {
        val authPossible = securityProvider.areAuthenticationMethodsAvailable() && requireAuth
        if (authPossible) { securityProvider.wasAuthenticated() } else { flowOf(true) }.collect {
            if (it) {
                val privateKey = securityProvider.keyStore.getKey(alias, null)
                if (privateKey != null) {
                    this@flow.emit(AndroidKey(privateKey, usePadding))
                    return@collect
                }

                generateKeyPair(alias, requireAuth).collect { keyPair ->
                    emit(keyPair.privateKey)
                }
            }
        }
    }

    override fun getOrCreatePublicKey(alias: String, requireAuth: Boolean): Flow<IKey> = flow {
        val authPossible = securityProvider.areAuthenticationMethodsAvailable() && requireAuth
        if (authPossible) { securityProvider.wasAuthenticated() } else { flowOf(true) }.collect {
            if (it) {
                val certificate = securityProvider.keyStore.getCertificate(alias)
                if (certificate != null) {
                    emit(AndroidKey(certificate.publicKey, usePadding))
                    return@collect
                }

                generateKeyPair(alias, requireAuth).collect { keyPair ->
                    emit(keyPair.publicKey)
                }
            }
        }
    }

    override fun createSignature(key: IKey, message: ByteArray): Flow<ByteArray> = flow {
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initSign((key as AndroidKey).rawKey as PrivateKey?)
        signature.update(message)
        emit(signature.sign())
    }

    override fun verifySignature(
        key: IKey,
        signature: ByteArray,
        original: ByteArray
    ): Boolean {
        val verifier = Signature.getInstance("SHA256withRSA")
        verifier.initVerify((key as AndroidKey).rawKey as PublicKey?)
        verifier.update(original)
        return verifier.verify(signature)
    }

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

    override fun generateKeyPair(alias: String, requireAuth: Boolean, signingKeys: Boolean): Flow<KeyPair> = flow {
        val authPossible = securityProvider.areAuthenticationMethodsAvailable() && requireAuth
        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA,
            AndroidSecurityProvider.KEY_STORE)
        val purpose = if (signingKeys) {
            KeyProperties.PURPOSE_VERIFY or KeyProperties.PURPOSE_SIGN
        } else {
            KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_ENCRYPT
        }
        keyPairGenerator.initialize(KeyGenParameterSpec.Builder(alias, purpose).run {
            setUserAuthenticationRequired(authPossible)
            setUserAuthenticationParameters(10000000, AndroidSecurityProvider.KEY_AUTH_REQUIRED)
            if (usePadding) {
                if (signingKeys) {
                    setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    setDigests(KeyProperties.DIGEST_SHA256)
                    setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                } else {
                    setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    setDigests(KeyProperties.DIGEST_SHA256)
                    setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                }
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

    override fun getName(): String = "RSA-4096"

}