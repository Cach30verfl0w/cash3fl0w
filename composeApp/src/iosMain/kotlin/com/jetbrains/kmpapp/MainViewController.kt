package com.jetbrains.kmpapp

import androidx.compose.ui.window.ComposeUIViewController
import de.cacheoverflow.cashflow.App

fun MainViewController() = ComposeUIViewController { App(
    App()
) }
