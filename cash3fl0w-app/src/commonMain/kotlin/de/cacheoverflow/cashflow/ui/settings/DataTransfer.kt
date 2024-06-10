/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.ui.settings

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import de.cacheoverflow.cashflow.ui.View
import de.cacheoverflow.cashflow.utils.dataTransfer

class DataTransferComponent(
    private val context: ComponentContext,
    internal val onBack: () -> Unit
): ComponentContext by context

@Composable
fun DataTransfer(component: DataTransferComponent) {
    View(dataTransfer(), onButton = component.onBack) {
    }
}