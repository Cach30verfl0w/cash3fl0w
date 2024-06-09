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

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

enum class ModalType {
    ERROR,
    WARNING,
    INFO
}

@Composable
fun Modal(
    visible: MutableState<Boolean>,
    onOk: () -> Unit = { visible.value = false },
    onDismiss: () -> Unit = { visible.value = false },
    title: String,
    type: ModalType,
    dismiss: Boolean = true,
    ok: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    if (visible.value) {
        Dialog(properties = DialogProperties(usePlatformDefaultWidth = false),
            onDismissRequest = {
                visible.value = false
            }, content = {
                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp)) {
                    Surface(
                        tonalElevation = 1.dp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(8.dp))
                            .border(Dp.Hairline, Color.Black, RoundedCornerShape(8.dp))
                    ) {
                        Column(modifier = Modifier.padding(15.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    when(type) {
                                        ModalType.ERROR -> Icons.Filled.Error
                                        ModalType.WARNING -> Icons.Filled.Warning
                                        ModalType.INFO -> Icons.Filled.Info
                                    },
                                    null,
                                    modifier = Modifier.size(35.dp)
                                )
                                Text(
                                    text = title,
                                    fontSize = 25.sp,
                                    modifier = Modifier.padding(PaddingValues(start = 10.dp))
                                )
                            }
                            content()
                            Row(modifier = Modifier.padding(PaddingValues(top = 10.dp))) {
                                if (dismiss) {
                                    IconButton(
                                        onClick = onDismiss,
                                        modifier = Modifier.border(1.dp, MaterialTheme.colorScheme
                                            .primary,
                                            RoundedCornerShape(6.dp))
                                    ) {
                                        Icon(Icons.Filled.Close, "Close")
                                    }
                                    Spacer(Modifier.width(5.dp))
                                }

                                if (ok) {
                                    IconButton(
                                        onClick = onOk,
                                        modifier = Modifier.border(1.dp, MaterialTheme.colorScheme
                                            .primary,
                                            RoundedCornerShape(6.dp))
                                    ) {
                                        Icon(Icons.Filled.Check, "Close")
                                    }
                                }
                            }
                        }
                    }
                }
            })
    }
}