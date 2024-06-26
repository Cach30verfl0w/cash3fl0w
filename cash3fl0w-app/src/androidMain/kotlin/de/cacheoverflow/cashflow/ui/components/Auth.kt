/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.ui.components

import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.biometric.BiometricPrompt.AuthenticationResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import de.cacheoverflow.cashflow.MainActivity
import de.cacheoverflow.cashflow.security.AndroidSecurityProvider
import de.cacheoverflow.cashflow.security.ISecurityProvider
import de.cacheoverflow.cashflow.utils.DI
import de.cacheoverflow.cashflow.utils.hardwareNotPresent
import de.cacheoverflow.cashflow.utils.noAuthenticationMethodsFound
import de.cacheoverflow.cashflow.utils.unknownError

sealed class AuthState {
    data object Authenticated : AuthState()
    data object AwaitingAuth: AuthState()
    data object AuthCancelled: AuthState()
    data class AuthNotPossible(val code: Int): AuthState()
}

// TODO: Require re-authentication after phone lock and unlock
@Composable
actual fun OptionalAuthLock(
    enabled: Boolean,
    title: String,
    subtitle: String,
    awaitAuth: @Composable () -> Unit,
    authNotPossible: @Composable (String) -> Unit,
    authCancelled: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    var authStateState by remember {
        mutableStateOf(
            if (enabled)
                AuthState.AwaitingAuth
            else
                AuthState.Authenticated
        )
    }
    when(val authState = authStateState) {
        is AuthState.AuthCancelled -> authCancelled()
        is AuthState.Authenticated -> {
            content()
        }
        is AuthState.AuthNotPossible -> {
            authNotPossible(when(authState.code) {
                BiometricPrompt.ERROR_NO_BIOMETRICS -> noAuthenticationMethodsFound()
                BiometricPrompt.ERROR_HW_NOT_PRESENT -> hardwareNotPresent()
                BiometricPrompt.ERROR_HW_UNAVAILABLE -> hardwareNotPresent()
                else -> unknownError()
            })
        }
        is AuthState.AwaitingAuth -> {
            MainActivity.instance?.apply {
                val executor = ContextCompat.getMainExecutor(LocalContext.current)
                val auths = Authenticators.DEVICE_CREDENTIAL or Authenticators.BIOMETRIC_STRONG
                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setAllowedAuthenticators(auths)
                    .build()
                BiometricPrompt(this, executor, object : AuthenticationCallback() {
                    override fun onAuthenticationError(code: Int, errString: CharSequence) {
                        authStateState = when(code) {
                            BiometricPrompt.ERROR_USER_CANCELED -> AuthState.AuthCancelled
                            else -> AuthState.AuthNotPossible(code)
                        }
                    }

                    override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                        authStateState = AuthState.Authenticated
                        (DI.inject<ISecurityProvider>() as AndroidSecurityProvider)
                            .isAuthenticated.value = true
                    }

                    override fun onAuthenticationFailed() {
                        // TODO: Handle failed
                    }
                }).authenticate(promptInfo)
            }
            awaitAuth()
        }
    }
}