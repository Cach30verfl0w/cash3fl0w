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

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.WindowManager.LayoutParams
import de.cacheoverflow.cashflow.MainActivity
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * This is the Android-specific implementation of the security manager. This security manager
 * implements the screenshot policy, authentication methods check and more. The cryptography
 * part of the manager supports creating strong keys and storing keys in a more secure storage
 * provided by the operating system.
 *
 * The cryptography module of Android allows the security manager to lock keys and key pairs
 * behind the authentication mechanism of Android. So you can lock keys behind the authentication
 * prompt shown on the start of the application.
 *
 * @author Cedric Hammes
 * @since  03/06/2024
 *
 * @see KeyStore
 * @see LayoutParams
 * @see KeyguardManager
 */
class AndroidSecurityProvider: ISecurityProvider {

    private val defaultKeyStore = KeyStore.getInstance(DEFAULT_KEY_STORE).apply { load(null) }
    private var screenshotDisabled = false

    /**
     * This method toggles the policy of disabling screenshots for this app. This is used to provide
     * more security to the financial information of the user against many forms of information
     * leakage.
     *
     * @author Cedric Hammes
     * @since  02/06/2024
     */
    override fun toggleScreenshotPolicy() {
        val window = MainActivity.instance?.window
        if (window != null) {
            this.screenshotDisabled = !this.screenshotDisabled
            when (this.screenshotDisabled) {
                true -> window.setFlags(LayoutParams.FLAG_SECURE, LayoutParams.FLAG_SECURE)
                false -> window.clearFlags(LayoutParams.FLAG_SECURE)
            }
        }
    }

    /**
     * This method checks whether the device has authentication methods like PIN etc. This check is
     * done by the system-specific component of the application.
     *
     * @author Cedric Hammes
     * @since  02/06/2024
     */
    override fun areAuthenticationMethodsAvailable(): Boolean {
        return MainActivity.instance?.getSystemService(KeyguardManager::class.java)?.isKeyguardSecure
            ?: false
    }

    /**
     * This method returns whether the screenshot policy setting is supported or not. If not, the
     * 'disable screenshots' setting is grayed out.
     *
     * @author Cedric Hammes
     * @since  02/06/2024
     */
    @SuppressLint("ObsoleteSdkInt")
    override fun isScreenshotPolicySupported(): Boolean {
        return true // This feature was added in API level 1 so it's supported
    }

    /**
     * This method queries the RSA key pair by the name specified. If the RSA key pair is present,
     * the key is returned. Otherwise this method creates a key pair generator with the specified
     * configuration and generates a key. That key pair is stored in the keystore and is returned
     * to the user.
     *
     * @param name         The name of the keypair in the keystore
     * @param padding      Use encryption padding or not
     * @param needUserAuth Whether user authentication is required to unlock keypair
     *
     * @author Cedric Hammes
     * @since  03/06/2024
     */
    private fun getOrCreateRSAKey(
        name: String,
        padding: Boolean = true,
        needUserAuth: Boolean = true
    ): KeyPair {
        val queriedPrivateKey = this.defaultKeyStore.getKey(name, null) as PrivateKey?
        val queriedPublicKey = this.defaultKeyStore.getCertificate(name).publicKey
        if (queriedPrivateKey == null || queriedPublicKey == null) {
            return KeyPair(queriedPublicKey, queriedPrivateKey)
        }

        val keyPurpose = KeyProperties.PURPOSE_DECRYPT
        val keyGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        keyGenerator.initialize(KeyGenParameterSpec.Builder(DEFAULT_KEY_STORE, keyPurpose)
            .setUserAuthenticationRequired(needUserAuth)
            .setEncryptionPaddings(if (padding) {
                KeyProperties.ENCRYPTION_PADDING_RSA_OAEP
            } else {
                KeyProperties.ENCRYPTION_PADDING_NONE
            }).apply {
                if (padding) {
                    setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                }
            }
            .build())
        return keyGenerator.generateKeyPair()
    }

    /**
     * This method queries the AES key by the name specified. If the RSA key is present, the key is
     * returned. Otherwise this method creates a key generator with the specified configuration and
     * generates a key. That key is stored in the keystore and is returned to the user.
     *
     * @param name         The name of the key in the keystore
     * @param padding      Use encryption padding or not
     * @param needUserAuth Whether user authentication is required to unlock key
     *
     * @author Cedric Hammes
     * @since  03/06/2024
     */
    private fun getOrCreateAESKey(
        name: String,
        padding: Boolean = true,
        needUserAuth: Boolean = true
    ): SecretKey {
        val queriedKey = this.defaultKeyStore.getKey(name, null) as SecretKey?
        if (queriedKey != null) {
            return queriedKey
        }

        val keyPurpose = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES)
        keyGenerator.init(KeyGenParameterSpec.Builder(DEFAULT_KEY_STORE, keyPurpose)
            .setUserAuthenticationRequired(needUserAuth)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(if (padding) {
                KeyProperties.ENCRYPTION_PADDING_PKCS7
            } else {
                KeyProperties.ENCRYPTION_PADDING_NONE
            })
            .build())
        return keyGenerator.generateKey()
    }

    companion object {
        /**
         * This is the name of the keystore used by default to store keys and key-pairs of the
         * application. This mechanism of Android provides a higher security of critical info
         * like financial data.
         */
        private const val DEFAULT_KEY_STORE = "AndroidKeyStore"
    }

}