/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.security

import de.cacheoverflow.cashflow.security.cryptography.IAsymmetricCryptoProvider
import de.cacheoverflow.cashflow.security.cryptography.ISymmetricCryptoProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import okio.Path

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

    fun getSymmetricCryptoProvider(usePadding: Boolean = true): ISymmetricCryptoProvider

    fun getAsymmetricCryptoProvider(usePadding: Boolean = true): IAsymmetricCryptoProvider

    /**
     * This method reads the specified file from the file system specified in the constructor to
     * extract a key from the file.
     *
     * @param file       The relative path to the file being read
     * @param algorithm  The key's algorithm
     * @param privateKey Whether the key is a private key or not (ignored on symmetric algorithms)
     *
     * @author Cedric Hammes
     * @since  05/06/2024
     */
    fun readKeyFromFile(
        file: Path,
        algorithm: EnumAlgorithm,
        privateKey: Boolean = true,
        usePadding: Boolean = true
    ): Flow<IKey>

    /**
     * This method returns the authentication methods supported by this system. This is used to
     * reduce the amount of settings based on the authentication schemes available.
     *
     * @author Cedric Hammes
     * @since  10/06/2024
     */
    fun getSupportedAuthMethods(): Array<EnumAuthScheme>

    /**
     * This method returns the status of the specified authentication method of this device. This
     * can be used to check whether the respective authentication method is supported.
     *
     * @author Cedric Hammes
     * @since  06/06/2024
     */
    fun isAuthenticationSupported(authScheme: EnumAuthScheme): EnumAuthStatus

    /**
     * This method returns whether the application's environment is rooted/jailbreaked to show the
     * user a prompt about the security of sensitive data.
     *
     * @author Cedric Hammes
     * @since  09/06/2024
     */
    fun isDeviceRooted(): Boolean

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
     * This method returns whether the user was successfully authenticated or not. This is mainly
     * used for visual effects and some load logic. The keys are not locked behind this simple
     * boolean so the app provides more security for the credentials than a simple lock.
     *
     * @author Cedric Hammes
     * @since  04/06/2024
     */
    fun wasAuthenticated(): StateFlow<Boolean>

    /**
     * This method returns whether the screenshot policy setting is supported or not. If not, the
     * 'disable screenshots' setting is grayed out.
     *
     * @author Cedric Hammes
     * @since  02/06/2024
     */
    fun isScreenshotPolicySupported(): Boolean

    /**
     * This method returns whether screenshots are allowed or not. This is used for the settings
     * component display.
     *
     * @author Cedric Hammes
     * @since  04/06/2024
     */
    fun areScreenshotsDisallowed(): Boolean

    /**
     * This enum represents all algorithms supported by the crypto provider
     * @author Cedric Hammes
     * @since  05/06/2024
     */
    enum class EnumAlgorithm {
        AES,
        RSA
    }

}