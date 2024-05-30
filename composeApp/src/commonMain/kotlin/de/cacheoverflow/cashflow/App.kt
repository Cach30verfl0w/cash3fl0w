package de.cacheoverflow.cashflow

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import de.cacheoverflow.cashflow.navigation.RootComponent
import de.cacheoverflow.cashflow.ui.Modal
import de.cacheoverflow.cashflow.ui.ModalType

@Composable
fun App() {
    val root = RootComponent(DefaultComponentContext(LifecycleRegistry()))
    MaterialTheme {
        val childStack by root.childStack.subscribeAsState()
        Children(stack = childStack, animation = stackAnimation(fade())) {
            child -> when(child.instance) {
                is RootComponent.Child.MainScreen -> ScreenA(root)
                is RootComponent.Child.TestScreen -> ScreenB()
            }
        }
        Modal(title = "Test", type = ModalType.WARNING, ok = true) {
            Text("Meine Parenten sind am Ende mit der Rente")
        }
    }
}

@Composable
@OptIn(ExperimentalDecomposeApi::class)
fun ScreenA(root: RootComponent) {
    Button(onClick = { root.navigation.pushNew(RootComponent.Configuration.TestScreen) }) {
        Text(text = "Test")
    }
}

@Composable
fun ScreenB() {
    Text(text = "Second")
}