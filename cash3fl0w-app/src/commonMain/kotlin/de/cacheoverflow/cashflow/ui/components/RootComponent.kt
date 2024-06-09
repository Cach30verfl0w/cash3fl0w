/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.ui.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import de.cacheoverflow.cashflow.ui.HomeScreenComponent
import de.cacheoverflow.cashflow.ui.settings.AuthSettingsComponent
import de.cacheoverflow.cashflow.ui.settings.SettingsComponent
import kotlinx.serialization.Serializable

class RootComponent(componentContext: ComponentContext): ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.MainMenu,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(config: Configuration, context: ComponentContext): Child {
        return when(config) {
            is Configuration.MainMenu -> Child.MainMenu(HomeScreenComponent(
                context,
                onButton = { navigation.push(Configuration.Settings) }
            ))
            is Configuration.Settings -> Child.Settings(SettingsComponent(
                context,
                onBack = { navigation.pop() },
                changeToAuthSettings = { navigation.push(Configuration.AuthSettings) }
            ))
            is Configuration.AuthSettings -> Child.AuthSettings(AuthSettingsComponent(
                context,
                onBack = { navigation.pop() }
            ))
        }
    }

    sealed class Child {
        data class MainMenu(val component: HomeScreenComponent) : Child()
        data class Settings(val component: SettingsComponent) : Child()
        data class AuthSettings(val component: AuthSettingsComponent) : Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object MainMenu: Configuration()
        @Serializable
        data object Settings: Configuration()
        @Serializable
        data object AuthSettings: Configuration()
    }
}