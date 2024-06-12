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

package io.karma.advcrypto.algorithm.delegates

import io.karma.advcrypto.wrapper.Hasher

class HasherDelegate<C> {
    private lateinit var initialize: () -> C
    private lateinit var hash: (C, ByteArray) -> String
    private var close: (CipherContext<C>) -> Unit = {}

    fun createHasher(): Hasher {
        return object: Hasher {
            val context = initialize()
            override fun hash(data: ByteArray): String = hash(context, data)
            override fun close() { close() }
        }
    }

    fun initialize(closure: () -> C) {
        this.initialize = closure
    }

    fun hash(closure: (C, ByteArray) -> String) {
        this.hash = closure
    }

    /**
     * This method sets a delegate to the close function of the hasher.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun close(closure: (context: CipherContext<C>) -> Unit) {
        this.close = closure
    }
}