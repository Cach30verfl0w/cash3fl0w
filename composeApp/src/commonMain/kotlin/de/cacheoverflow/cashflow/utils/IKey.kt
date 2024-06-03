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

package de.cacheoverflow.cashflow.utils

interface IKey {

    /**
     * This method returns the name/alias of the key. This was specified while key generation in the
     * security provider.
     *
     * @author Cedric Hammes
     * @since  03/06/2024
     */
    fun name(): String

    /**
     * This method returns the algorithm the key is used for, specified while key generation in the
     * security provider.
     *
     * @author Cedric Hammes
     * @since  03/06/2024
     */
    fun algorithm(): EnumAlgorithm

    /**
     * This method returns whether padding was used to generate the key. This was specified while
     * key generation in the security provider.
     *
     * @author Cedric Hammes
     * @since  03/06/2024
     */
    fun paddingUsed(): Boolean

    enum class EnumAlgorithm {
        AES,
        RSA
    }

}