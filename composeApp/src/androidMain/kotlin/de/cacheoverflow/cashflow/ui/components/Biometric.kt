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

package de.cacheoverflow.cashflow.ui.components

import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.biometric.BiometricPrompt.AuthenticationResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.core.content.ContextCompat
import de.cacheoverflow.cashflow.MainActivity
import de.cacheoverflow.cashflow.utils.authenticationCancelled
import de.cacheoverflow.cashflow.utils.authenticationNotPossible
import de.cacheoverflow.cashflow.utils.awaitingAuthentication
import de.cacheoverflow.cashflow.utils.hardwareNotPresent
import de.cacheoverflow.cashflow.utils.noAuthenticationMethodsFound
import de.cacheoverflow.cashflow.utils.unknownError

sealed class AuthState {
    data object Authenticated : AuthState()
    data object AwaitingAuth: AuthState()
    data class AuthNotPossible(val code: Int): AuthState()
}

@Composable
actual fun BiometricAuthLock(title: String, subtitle: String, content: @Composable () -> Unit) {
    var authStateState by remember { mutableStateOf<AuthState>(AuthState.AwaitingAuth) }
    when(val authState = authStateState) {
        is AuthState.Authenticated -> content()
        is AuthState.AuthNotPossible -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(authenticationNotPossible(), fontSize = 4.5.em)
                Text(when(authState.code) {
                    BiometricPrompt.ERROR_NO_BIOMETRICS -> noAuthenticationMethodsFound()
                    BiometricPrompt.ERROR_HW_NOT_PRESENT -> hardwareNotPresent()
                    BiometricPrompt.ERROR_HW_UNAVAILABLE -> hardwareNotPresent()
                    BiometricPrompt.ERROR_USER_CANCELED -> authenticationCancelled()
                    else -> unknownError()
                })
                Spacer(Modifier.height(20.dp))
                Icon(
                    Icons.Filled.Error,
                    modifier = Modifier.size(100.dp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        is AuthState.AwaitingAuth -> {
            MainActivity.instance?.apply {
                val executor = ContextCompat.getMainExecutor(LocalContext.current)
                val auths = Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL
                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setAllowedAuthenticators(auths)
                    .build()
                BiometricPrompt(this, executor, object : AuthenticationCallback() {
                    override fun onAuthenticationError(code: Int, errString: CharSequence) {
                        authStateState = AuthState.AuthNotPossible(code)
                    }

                    override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                        authStateState = AuthState.Authenticated
                    }

                    override fun onAuthenticationFailed() {
                        // TODO: Handle failed
                    }
                }).authenticate(promptInfo)
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(awaitingAuthentication(), fontSize = 4.5.em)
                Spacer(Modifier.height(20.dp))
                CircularProgressIndicator(modifier = Modifier.size(100.dp), strokeWidth = 10.dp)
            }
        }
    }
}