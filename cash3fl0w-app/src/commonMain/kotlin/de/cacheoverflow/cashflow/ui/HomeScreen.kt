/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import de.cacheoverflow.cashflow.security.ISecurityProvider
import de.cacheoverflow.cashflow.utils.DI
import de.cacheoverflow.cashflow.utils.home
import de.cacheoverflow.cashflow.utils.keyringNotUnlocked
import de.cacheoverflow.cashflow.utils.loadingAccounts

class HomeScreenComponent(
    private val context: ComponentContext,
    internal val onButton: () -> Unit
): ComponentContext by context

@Composable
fun HomeScreen(component: HomeScreenComponent) {
    // TODO: Add option in view to disable button and disable button if accounts were not loaded
    View(home(), false, onButton = component.onButton) {
            Column(modifier = Modifier.fillMaxSize()) {
                if (!DI.inject<ISecurityProvider>().wasAuthenticated().value) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        contentAlignment = Alignment.Center
                    ) {
                        // TODO: Add clickable text for re-authentication
                        Text(
                            text = keyringNotUnlocked(),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // TODO: This is shown while waiting for the accounts to be decrypted and loaded
                    //    They are loaded after a connection to the bank was established
                    Text(
                        loadingAccounts(),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(20.dp))
                    CircularProgressIndicator(modifier = Modifier.size(100.dp), strokeWidth = 10.dp)
                }
                Spacer(modifier = Modifier.weight(1f))
            }
    }
}