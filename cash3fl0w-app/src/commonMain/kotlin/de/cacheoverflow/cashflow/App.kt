/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import de.cacheoverflow.cashflow.api.tradingview.EnumMarket
import de.cacheoverflow.cashflow.api.tradingview.TradingViewAPI
import de.cacheoverflow.cashflow.security.ISecurityProvider
import de.cacheoverflow.cashflow.ui.DefaultColorScheme
import de.cacheoverflow.cashflow.ui.HomeScreen
import de.cacheoverflow.cashflow.ui.components.Modal
import de.cacheoverflow.cashflow.ui.components.ModalType
import de.cacheoverflow.cashflow.ui.settings.Settings
import de.cacheoverflow.cashflow.ui.components.RootComponent
import de.cacheoverflow.cashflow.utils.DI
import de.cacheoverflow.cashflow.utils.defaultCoroutineScope
import de.cacheoverflow.cashflow.utils.deviceRootedPromptText
import de.cacheoverflow.cashflow.utils.securityWarning
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.dsl.module

interface IErrorHandler {
    val error: MutableStateFlow<Exception?>
}

val sharedModule = module {
    single<IErrorHandler> {
        object: IErrorHandler {
            override val error: MutableStateFlow<Exception?> = MutableStateFlow(null)
        }
    }
}

@Composable
fun AppView(
    childStack: ChildStack<RootComponent.Configuration, RootComponent.Child>
) {
    Children(childStack, animation = stackAnimation(slide())) {
        when (val instance = it.instance) {
            is RootComponent.Child.MainMenu -> HomeScreen(instance.component)
            is RootComponent.Child.Settings -> Settings(instance.component)
        }
    }
}

@Composable
fun App(root: RootComponent) {
    MaterialTheme(colorScheme = DefaultColorScheme) {
        val childStack by root.childStack.subscribeAsState()

        val json = Json { ignoreUnknownKeys = true }
        defaultCoroutineScope.launch {
            TradingViewAPI(HttpClient {
                install(ContentNegotiation) {
                    json(json, contentType = ContentType.Any)
                }
            }).scanMarket(EnumMarket.AMERICA)
        }

        // TODO: Lock AppView behind authentication lock
        if (DI.inject<ISecurityProvider>().isDeviceRooted()) {
            // TODO: Don't show menu if already clicked away?
            val rootedModalVisible = remember { mutableStateOf(true) }
            Modal(rootedModalVisible, securityWarning(), ModalType.WARNING) {
                Text(deviceRootedPromptText())
            }
        }

        AppView(childStack)
    }
}
