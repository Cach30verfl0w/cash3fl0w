/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import de.cacheoverflow.cashflow.ui.View
import de.cacheoverflow.cashflow.ui.components.ClickSetting
import de.cacheoverflow.cashflow.ui.components.SettingsGroup
import de.cacheoverflow.cashflow.utils.dataTransfer
import de.cacheoverflow.cashflow.utils.initTransfer
import de.cacheoverflow.cashflow.utils.receiveTransfer
import de.cacheoverflow.cashflow.utils.regenerateSecret
import de.cacheoverflow.cashflow.utils.secret
import de.cacheoverflow.cashflow.utils.server
import qrgenerator.QRCodeImage

class DataTransferComponent(
    private val context: ComponentContext,
    internal val onBack: () -> Unit
): ComponentContext by context

@Composable
fun DataTransfer(component: DataTransferComponent) {
    View(dataTransfer(), onButton = component.onBack) {
        Column {
            SettingsGroup(null, null) {
                Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
                    Column {
                        QRCodeImage(
                            url = "https://google.com".repeat(20),
                            contentDescription = null,
                            modifier = Modifier.size(150.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    "Der Server läuft nicht",
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Spacer(Modifier.width(20.dp))
                            Text(
                                server("127.0.0.1:1337"), // TODO: Replace with chosen
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                            Text(
                                secret("05980495848555"), // TODO: Replace with chosen
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                        Button(
                            shape = ShapeDefaults.Small,
                            modifier = Modifier.padding(top = 5.dp).width(200.dp),
                            onClick = {
                                // TODO: Regenerate secret
                            }
                        ) {
                            Text(
                                regenerateSecret(),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
            SettingsGroup(null, null) {
                ClickSetting(initTransfer()) {
                    // TODO: Change after press to cancel
                    // TODO: Open separate menu
                }
                ClickSetting(receiveTransfer()) {
                    // TODO: Open separate menu
                }
            }
        }
    }
}