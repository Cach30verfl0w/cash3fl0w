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

package de.cacheoverflow.cashflow.store

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

val defaultCoroutineScope = CoroutineScope(Dispatchers.Default)

enum class EnumTheme {
    SYSTEM,
    WHITE,
    DARK
}

data class CashFlowSettings(
    val theme: EnumTheme
)

interface CashFlowSettingsHolder: StateFlow<CashFlowSettings> {
    fun update(updater: (CashFlowSettings) -> CashFlowSettings)
}

// Acquire information from DataStore or other config system
class DefaultCashFlowSettingsHolder(
    private val flow: MutableStateFlow<CashFlowSettings> = MutableStateFlow(CashFlowSettings(EnumTheme.SYSTEM))
): CashFlowSettingsHolder {
    override suspend fun collect(collector: FlowCollector<CashFlowSettings>): Nothing {
        this.flow.collect(collector)
    }
    
    override fun update(updater: (CashFlowSettings) -> CashFlowSettings) {
        defaultCoroutineScope.launch {
            flow.emit(updater(flow.value))
        }
    }

    override val replayCache: List<CashFlowSettings>
        get() = flow.replayCache
    override val value: CashFlowSettings
        get() = flow.value

}