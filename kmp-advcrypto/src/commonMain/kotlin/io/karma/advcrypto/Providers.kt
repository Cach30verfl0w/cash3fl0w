/*
 * Copyright (c) 2024 Cach30verfl0w
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

package io.karma.advcrypto

import io.karma.advcrypto.algorithm.Algorithm

object Providers {
    private val registeredProviders: MutableList<AbstractProvider> = ArrayList()

    init {
        initDefaultPlatformProvider()
    }

    fun addProvider(provider: AbstractProvider) {
        this.registeredProviders.add(provider)
    }

    fun getProviderByName(name: String): AbstractProvider? = this.registeredProviders
        .firstOrNull { it.name == name }

    fun getAlgorithmByName(name: String): Algorithm? = this.registeredProviders
        .map { it.getAlgorithms() }.flatten().firstOrNull { it.name == name }
}

internal expect fun initDefaultPlatformProvider()