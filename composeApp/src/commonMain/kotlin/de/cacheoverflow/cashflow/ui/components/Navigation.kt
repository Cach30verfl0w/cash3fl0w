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

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import de.cacheoverflow.cashflow.MainMenuComponent
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
            Configuration.MainMenu -> Child.MainMenu(MainMenuComponent(context))
        }
    }

    sealed class Child {
        data class MainMenu(val component: MainMenuComponent): Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object MainMenu: Configuration()
    }
}