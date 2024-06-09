/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.security.cryptography

import de.cacheoverflow.cashflow.security.IKey
import kotlinx.coroutines.flow.Flow

/**
 * This interface provides the implementation of symmetric cryptography algorithms like AES-256 in
 * the CashFlow application.
 *
 * @author Cedric Hammes
 * @since  05/06/2024
 */
interface ISymmetricCryptoProvider {

    // TODO: getOrGenerate

    fun encrypt(key: IKey, message: ByteArray): Flow<ByteArray>

    fun decrypt(key: IKey, message: ByteArray): Flow<ByteArray>

    fun generateKey(
        alias: String,
        requireAuth: Boolean = true
    ): Flow<IKey>

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