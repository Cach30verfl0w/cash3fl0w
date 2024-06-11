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

package io.karma.advcrypto.algorithm.specs

import io.karma.advcrypto.algorithm.BlockMode
import io.karma.advcrypto.algorithm.Padding
import io.karma.advcrypto.keys.Key

data class CipherSpec(
    val key: Key,
    val blockMode: BlockMode?,
    val padding: Padding
) {
    class Builder(private val key: Key) {
        private var blockMode: BlockMode? = null
        private var padding: Padding = Padding.NONE

        fun setBlockMode(blockMode: BlockMode): Builder = apply { this.blockMode = blockMode }
        fun setPadding(padding: Padding): Builder = apply { this.padding = padding }
        fun build(): CipherSpec = CipherSpec(key, blockMode, padding)
    }
}