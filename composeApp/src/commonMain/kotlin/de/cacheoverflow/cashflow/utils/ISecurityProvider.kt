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

/**
 * This interface is the implementation templates for the security and cryptography architecture of
 * the Cash3Fl0w finance app. This provider is used to acquire security-related information or
 * change security-related configuration.
 *
 * Also this interface provides a platform-independent wrapper for cryptographic operations like
 * encrypt and decrypt content etc.
 *
 * @author Cedric Hammes
 * @since  02/06/2024
 */
interface ISecurityProvider {

    /**
     * This method creates acquires key with the specified parameters from the keystore or if no key
     * found, the security manager creates a new key and stores it in the keystore of the target
     * system.
     *
     * @author Cedric Hammes
     * @since  03/04/2024
     */
    fun getOrCreateKey(name: String,
        algorithm: IKey.EnumAlgorithm,
        padding: Boolean = true,
        needUserAuth: Boolean = true,
        privateKey: Boolean = false
    ): IKey

    /**
     * This method toggles the policy of disabling screenshots for this app. This is used to provide
     * more security to the financial information of the user against many forms of information
     * leakage.
     *
     * @author Cedric Hammes
     * @since  02/06/2024
     */
    fun toggleScreenshotPolicy()

    /**
     * This method checks whether the device has authentication methods like PIN etc. This check is
     * done by the system-specific component of the application.
     *
     * @author Cedric Hammes
     * @since  02/06/2024
     */
    fun areAuthenticationMethodsAvailable(): Boolean

    /**
     * This method returns whether the screenshot policy setting is supported or not. If not, the
     * 'disable screenshots' setting is grayed out.
     *
     * @author Cedric Hammes
     * @since  02/06/2024
     */
    fun isScreenshotPolicySupported(): Boolean

}