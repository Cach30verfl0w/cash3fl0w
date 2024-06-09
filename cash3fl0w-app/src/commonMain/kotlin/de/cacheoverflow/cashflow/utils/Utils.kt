/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatformTools

val defaultCoroutineScope = CoroutineScope(Dispatchers.Default)
val ioCoroutineScope = CoroutineScope(Dispatchers.IO)

object DI {
    inline fun <reified T: Any> inject(): T = KoinPlatformTools.defaultContext().get().get<T>()
}

fun <T> Flow<T>.collectAsync(collector: FlowCollector<T>) = defaultCoroutineScope.launch {
    this@collectAsync.collect(collector)
}
