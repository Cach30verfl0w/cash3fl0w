/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.utils

import de.cacheoverflow.cashflow.security.EnumAuthScheme
import de.cacheoverflow.cashflow.security.EnumAuthStatus
import de.cacheoverflow.cashflow.security.ISecurityProvider
import de.cacheoverflow.cashflow.security.cryptography.IAsymmetricCryptoProvider
import de.cacheoverflow.cashflow.security.cryptography.ISymmetricCryptoProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okio.Path

class DesktopSecurityProvider: ISecurityProvider {

    override fun getSymmetricCryptoProvider(usePadding: Boolean): ISymmetricCryptoProvider {
        TODO("Not yet implemented")
    }

    override fun getAsymmetricCryptoProvider(usePadding: Boolean): IAsymmetricCryptoProvider {
        TODO("Not yet implemented")
    }

    override fun readKeyFromFile(
        file: Path,
        algorithm: ISecurityProvider.EnumAlgorithm,
        privateKey: Boolean,
        usePadding: Boolean
    ): Flow<IKey> {
        TODO("Not yet implemented")
    }

    override fun getSupportedAuthMethods(): Array<EnumAuthScheme> {
        return EnumAuthScheme.entries.toTypedArray() // TODO
    }

    override fun isAuthenticationSupported(authScheme: EnumAuthScheme): EnumAuthStatus {
        return when(authScheme) {
            EnumAuthScheme.CREDENTIAL -> EnumAuthStatus.SUPPORTED
            EnumAuthScheme.FINGERPRINT -> EnumAuthStatus.UNSUPPORTED // TODO: Check support
            EnumAuthScheme.FACE_DETECTION -> EnumAuthStatus.UNSUPPORTED // TODO: Check support
        }
    }

    override fun isDeviceRooted(): Boolean {
        return false
    }

    override fun toggleScreenshotPolicy() {
        throw UnsupportedOperationException("You can't toggle screenshot policy on Desktop")
    }

    override fun areAuthenticationMethodsAvailable(): Boolean {
        return true // Authentication like passwords are always available
    }

    override fun wasAuthenticated(): StateFlow<Boolean> {
        return MutableStateFlow(true) // TODO: Lock behind authentication
    }

    override fun isScreenshotPolicySupported(): Boolean {
        return false // Blocking screenshots for an application is not possible on desktop
    }

    override fun areScreenshotsDisallowed(): Boolean {
        return false // Disallowing screenshots is not possible
    }

}