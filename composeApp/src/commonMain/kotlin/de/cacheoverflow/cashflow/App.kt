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
        Modal(title = "Test", type = ModalType.INFO, ok = true) {
            Text("This is just an experiment")
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