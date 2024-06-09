/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.ui.components

import androidx.compose.runtime.Composable

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
    // TODO: Implement support for password or biometric authentication
    content()
}