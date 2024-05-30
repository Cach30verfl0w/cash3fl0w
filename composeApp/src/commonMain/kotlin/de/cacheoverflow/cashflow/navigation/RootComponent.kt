package de.cacheoverflow.cashflow.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import kotlinx.serialization.Serializable

class RootComponent(componentContext: ComponentContext): ComponentContext by componentContext {
    val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.MainScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(config: Configuration, context: ComponentContext): Child {
        return when(config) {
            Configuration.MainScreen -> Child.MainScreen(MainScreenComponent(context))
            Configuration.TestScreen -> Child.TestScreen(TestScreenComponent(context))
        }
    }

    /**
     * A screen
     */
    sealed class Child {
        data class MainScreen(val component: MainScreenComponent): Child()
        data class TestScreen(val component: TestScreenComponent): Child()
    }

    /**
     * Configurations define parameters given to the "new" screen
     */
    @Serializable
    sealed class Configuration {
        @Serializable
        data object MainScreen: Configuration()

        @Serializable
        data object TestScreen: Configuration()
    }
}
