/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.security.cryptography

import de.cacheoverflow.cashflow.security.IKey
import de.cacheoverflow.cashflow.security.KeyPair
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

/**
 * This interface provides the implementation of symmetric cryptography algorithms like AES-256 in
 * the CashFlow application.
 *
 * @author Cedric Hammes
 * @since  05/06/2024
 */
interface IAsymmetricCryptoProvider {

    fun getOrCreateKeyPair(alias: String, requireAuth: Boolean = true): Flow<KeyPair> = combine(
        getOrCreatePublicKey(alias, requireAuth),
        getOrCreatePrivateKey(alias, requireAuth)
    ) { publicKey, privateKey -> KeyPair(publicKey, privateKey) }

    fun getOrCreatePrivateKey(alias: String, requireAuth: Boolean = true): Flow<IKey>

    fun getOrCreatePublicKey(alias: String, requireAuth: Boolean = true): Flow<IKey>

    fun createSignature(key: IKey, message: ByteArray): Flow<ByteArray>

    fun verifySignature(key: IKey, signature: ByteArray, original: ByteArray): Boolean

    fun encrypt(key: IKey, message: ByteArray): Flow<ByteArray>

    fun decrypt(key: IKey, message: ByteArray): Flow<ByteArray>

    fun generateKeyPair(
        alias: String,
        requireAuth: Boolean = true,
        signingKeys: Boolean = true
    ): Flow<KeyPair>

    /**
     * This method returns the name of the symmetric crypto provider with information about padding
     * or block mode.
     *
     * @author Cedric Hammes
     * @since  05/06/2024
     */
    fun getAlgorithm(): String

    /**
     * This method returns the name of the symmetric crypto provider without information about
     * padding or block mode.
     *
     * @author Cedric Hammes
     * @since  05/06/2024
     */
    fun getName(): String
}