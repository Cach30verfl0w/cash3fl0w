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

import android.content.pm.PackageManager
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
import androidx.compose.material3.CircularProgressIndicator
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

enum class EnumAuthState {
    AUTHENTICATED,
    AWAITING_AUTH
}

@Composable
actual fun BiometricAuthLock(title: String, subtitle: String, content: @Composable () -> Unit) {
    var authState by remember { mutableStateOf(EnumAuthState.AWAITING_AUTH) }
    when(authState) {
        EnumAuthState.AUTHENTICATED -> content()
        EnumAuthState.AWAITING_AUTH -> {
            // TODO: LocalContext.current.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
            // TODO: Prompt cannot be shown if no PIN or other auth method is configured
            MainActivity.instance?.apply {
                val executor = ContextCompat.getMainExecutor(LocalContext.current)
                val auths = Authenticators.BIOMETRIC_STRONG or Authenticators.DEVICE_CREDENTIAL
                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setAllowedAuthenticators(auths)
                    .build()
                BiometricPrompt(this, executor, object : AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        // TODO: Handle error
                    }

                    override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                        authState = EnumAuthState.AUTHENTICATED
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
                Text("Awaiting authentication", fontSize = 4.5.em)
                Spacer(Modifier.height(20.dp))
                CircularProgressIndicator(modifier = Modifier.size(100.dp), strokeWidth = 10.dp)
            }
        }
    }
}