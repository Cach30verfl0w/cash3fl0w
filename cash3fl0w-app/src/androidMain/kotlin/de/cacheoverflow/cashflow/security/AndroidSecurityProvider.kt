/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.security

import android.app.KeyguardManager
import android.content.Context
import android.security.keystore.KeyProperties
import android.view.WindowManager.LayoutParams
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import de.cacheoverflow.cashflow.MainActivity
import de.cacheoverflow.cashflow.utils.DI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okio.Path
import java.io.IOException
import java.util.ArrayList

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
 * @see LayoutParams
 * @see KeyguardManager
 */
class AndroidSecurityProvider(private val pathProvider: (Path) -> Path): ISecurityProvider {
    val isAuthenticated = MutableStateFlow(false)
    private var screenshotDisabled = false

    /**
     * This method returns the authentication methods supported by this system. This is used to
     * reduce the amount of settings based on the authentication schemes available.
     *
     * @author Cedric Hammes
     * @since  10/06/2024
     */
    override fun getSupportedAuthMethods(): Array<EnumAuthScheme> {
        val supportedAuthMethods = ArrayList<EnumAuthScheme>()
        for (authenticationScheme in EnumAuthScheme.entries) {
            if (this.isAuthenticationSupported(authenticationScheme) != EnumAuthStatus.SUPPORTED) {
                continue
            }
            supportedAuthMethods.add(authenticationScheme)
        }
        return supportedAuthMethods.toTypedArray()
    }

    /**
     * This method toggles the policy of disabling screenshots for this app. This is used to provide
     * more security to the financial information of the user against many forms of information
     * leakage.
     *
     * @author Cedric Hammes
     * @since  02/06/2024
     */
    override fun toggleScreenshotPolicy() {
        val window = (DI.inject<Context>() as MainActivity).window
        if (window != null) {
            this.screenshotDisabled = !this.screenshotDisabled
            when (this.screenshotDisabled) {
                true -> window.setFlags(LayoutParams.FLAG_SECURE, LayoutParams.FLAG_SECURE)
                false -> window.clearFlags(LayoutParams.FLAG_SECURE)
            }
        }
    }

    /**
     * This method returns the status of the specified authentication method of this device. This
     * can be used to check whether the respective authentication method is supported.
     *
     * @author Cedric Hammes
     * @since  06/06/2024
     */
    override fun isAuthenticationSupported(authScheme: EnumAuthScheme): EnumAuthStatus {
        return when (BiometricManager.from(DI.inject()).canAuthenticate(when(authScheme) {
            EnumAuthScheme.FINGERPRINT -> Authenticators.BIOMETRIC_STRONG
            EnumAuthScheme.FACE_DETECTION -> Authenticators.BIOMETRIC_WEAK
            EnumAuthScheme.CREDENTIAL -> Authenticators.DEVICE_CREDENTIAL
        })) {
            BiometricManager.BIOMETRIC_SUCCESS -> EnumAuthStatus.SUPPORTED
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> EnumAuthStatus.HARDWARE_MISSING
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> EnumAuthStatus.HARDWARE_MISSING
            else -> EnumAuthStatus.UNSUPPORTED
        }
    }

    /**
     * This method returns whether the application's environment is rooted/jailbreaked to show the
     * user a prompt about the security of sensitive data.
     *
     * @author Cedric Hammes
     * @since  09/06/2024
     */
    override fun isDeviceRooted(): Boolean {
        try {
            Runtime.getRuntime().exec("su")
            return true
        } catch (ignored: IOException) {
            return MainActivity.instance?.packageManager?.getInstalledPackages(0)!!
                .count { it.applicationInfo.packageName == "com.topjohnwu.magisk" } > 0
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
        return DI.inject<Context>().getSystemService(KeyguardManager::class.java)?.isDeviceSecure
            ?: false
    }

    /**
     * This method returns whether the user was successfully authenticated or not. This is mainly
     * used for visual effects and some load logic. The keys are not locked behind this simple
     * boolean so the app provides more security for the credentials than a simple lock.
     *
     * @author Cedric Hammes
     * @since  04/06/2024
     */
    override fun wasAuthenticated(): StateFlow<Boolean> {
        return this.isAuthenticated
    }

    /**
     * This method returns whether the screenshot policy setting is supported or not. If not, the
     * 'disable screenshots' setting is grayed out.
     *
     * @author Cedric Hammes
     * @since  02/06/2024
     */
    override fun isScreenshotPolicySupported(): Boolean {
        return true // This feature was added in API level 1 so it's supported
    }

    /**
     * This method returns whether screenshots are allowed or not. This is used for the settings
     * component display.
     *
     * @author Cedric Hammes
     * @since  04/06/2024
     */
    override fun areScreenshotsDisallowed(): Boolean {
        return this.screenshotDisabled
    }

    companion object {
        /**
         * This is the name of the keystore used by default to store keys and key-pairs of the
         * application. This mechanism of Android provides a higher security of critical info
         * like financial data.
         */
        internal const val KEY_STORE = "AndroidKeyStore"

        /**
         * This is the flags of the authentication methods can be used to unlock the keys in the
         * keystore.
         */
        internal const val KEY_AUTH_REQUIRED = KeyProperties.AUTH_BIOMETRIC_STRONG or
                KeyProperties.AUTH_DEVICE_CREDENTIAL
    }
}