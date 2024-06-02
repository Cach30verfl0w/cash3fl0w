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

import androidx.compose.runtime.Composable
import de.cacheoverflow.cashflow.ui.AwaitAuth
import de.cacheoverflow.cashflow.ui.DefaultAuthNotPossible
import de.cacheoverflow.cashflow.utils.authenticationCancelled

/**
 * This composable represents the optional authentication lock. This makes it possible to lock the
 * specified content behind PIN or biometric authentication. This component also automatically
 * handles failed authentication like missing hardware or interrupted authentication. The
 * presentation of the error is by default defined.
 *
 * @param title           The title of the authentication prompt
 * @param subtitle        The subtitle of the authentication prompt
 * @param awaitAuth       The content to show while awaiting authentication form the user
 * @param authNotPossible The content to show if the authentication process throw's an error
 * @param authCancelled   The content to show if the authentication process was cancelled
 * @param content         The content to show after a successful authentication
 *
 * @author Cedric Hammes
 * @since  02/06/2024
 */
@Composable
expect fun OptionalAuthLock(
    enabled: Boolean = true,
    title: String,
    subtitle: String,
    awaitAuth: @Composable () -> Unit = { AwaitAuth() },
    authNotPossible: @Composable (String) -> Unit = { DefaultAuthNotPossible(it) },
    authCancelled: @Composable () -> Unit = { authNotPossible(authenticationCancelled()) },
    content: @Composable () -> Unit
)