/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import de.cacheoverflow.cashflow.ui.components.RootComponent

fun MainViewController() = ComposeUIViewController {
    val root = remember { RootComponent(DefaultComponentContext(LifecycleRegistry())) }
    App(root)
}
