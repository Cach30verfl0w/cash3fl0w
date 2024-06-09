/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.ui.components

import androidx.compose.runtime.Composable
import de.cacheoverflow.cashflow.ui.AwaitAuth
import de.cacheoverflow.cashflow.ui.DefaultAuthNotPossible

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
    authCancelled: @Composable () -> Unit,
    content: @Composable () -> Unit
)