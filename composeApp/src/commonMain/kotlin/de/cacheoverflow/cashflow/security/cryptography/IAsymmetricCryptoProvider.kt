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

import de.cacheoverflow.cashflow.security.IKey
import de.cacheoverflow.cashflow.security.KeyPair
import kotlinx.coroutines.flow.Flow

/**
 * This interface provides the implementation of symmetric cryptography algorithms like AES-256 in
 * the CashFlow application.
 *
 * @author Cedric Hammes
 * @since  05/06/2024
 */
interface IAsymmetricCryptoProvider {

    // TODO: getOrGenerate

    fun encrypt(key: IKey, message: ByteArray): Flow<ByteArray>

    fun decrypt(key: IKey, message: ByteArray): Flow<ByteArray>

    fun generateKeyPair(
        alias: String,
        requireAuth: Boolean = true
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