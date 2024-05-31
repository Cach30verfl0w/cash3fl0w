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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.cacheoverflow.cashflow.ui.SettingsGroup

enum class Menu {
    HOME,
    ACCOUNTS,
    SETTINGS
}

@Composable
fun Home() {
    Text("Home")
}

@Composable
fun Accounts() {
    Text("Accounts")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings() {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleLarge
                )
            })
        }
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(it).padding(16.dp)
        ) {
            SettingsGroup(name = "First") {
                Text("Hello World")
            }
            SettingsGroup(name = "Second") {
                Text("Hello World")
            }
        }
    }
}

@Composable
fun App() {
    var currentMenu by remember { mutableStateOf(Menu.HOME) }
    MaterialTheme {
        Box {
            when(currentMenu) {
                Menu.HOME -> Home()
                Menu.ACCOUNTS -> Accounts()
                Menu.SETTINGS -> Settings()
            }
        }
        Box(contentAlignment = Alignment.BottomStart, modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().background(Color(0xffc4c4c4))
            ) {
                IconButton(
                    onClick = { currentMenu = Menu.HOME },
                    modifier = Modifier.weight(1.0f),
                    enabled = currentMenu != Menu.HOME
                ) {
                    Icon(Icons.Filled.Home, "Home")
                }
                IconButton(
                    onClick = { currentMenu = Menu.ACCOUNTS },
                    modifier = Modifier.weight(1.0f),
                    enabled = currentMenu != Menu.ACCOUNTS
                ) {
                    Icon(Icons.Filled.AccountBox, "Home")
                }
                IconButton(
                    onClick = { currentMenu = Menu.SETTINGS },
                    modifier = Modifier.weight(1.0f),
                    enabled = currentMenu != Menu.SETTINGS
                ) {
                    Icon(Icons.Filled.Settings, "Home")
                }
            }
        }
    }
}
