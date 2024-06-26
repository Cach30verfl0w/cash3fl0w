/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import de.cacheoverflow.cashflow.utils.authenticationNotPossible
import de.cacheoverflow.cashflow.utils.awaitingAuthentication

@Composable
fun View(
    title: String,
    canGoBack: Boolean = true,
    onButton: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .height(55.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (canGoBack) {
                IconButton(onClick = onButton) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
            Text(
                title,
                fontSize = 23.sp, modifier = if (canGoBack) {
                    Modifier
                } else {
                    Modifier.padding(start = 10.dp)
                       },
                color = MaterialTheme.colorScheme.onSecondary
            )
            if (!canGoBack) {
                Spacer(Modifier.weight(1.0f))
                IconButton(onClick = onButton) {
                    Icon(
                        Icons.Filled.Menu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }

        }
        Box(modifier = Modifier.padding(top = 10.dp).fillMaxSize()) {
            content()
        }
    }
}

@Composable
fun DefaultAuthNotPossible(message: String) {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            authenticationNotPossible(),
            fontSize = 4.5.em,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(message, color = MaterialTheme.colorScheme.onSecondary)
        Spacer(Modifier.height(20.dp))
        Icon(
            Icons.Filled.Error,
            modifier = Modifier.size(100.dp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun AwaitAuth() {
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            awaitingAuthentication(),
            fontSize = 4.5.em,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Spacer(Modifier.height(20.dp))
        CircularProgressIndicator(modifier = Modifier.size(100.dp), strokeWidth = 10.dp)
    }
}