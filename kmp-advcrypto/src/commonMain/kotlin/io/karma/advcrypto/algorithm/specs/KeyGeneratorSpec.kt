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

/**
 * This class represents the specification data for the key generator. These information is used to
 * specify more information about the keys generated. The following information fields are
 * available:
 * - [KeyGeneratorSpec.purposes] (required): The purposes of the key
 * - [KeyGeneratorSpec.blockMode] (optional): The cipher's block mode
 * - [KeyGeneratorSpec.padding] (optional): The padding of the key
 * - [KeyGeneratorSpec.keySize] (optional): The key size in bits
 *
 * @author Cedric Hames
 * @since  11/06/2024
 */
data class KeyGeneratorSpec(
    val purposes: UByte,
    val blockMode: BlockMode?,
    val padding: Padding,
    val keySize: Int?
) {
    class Builder(private val purposes: UByte) {
        private var blockMode: BlockMode? = null
        private var padding: Padding = Padding.NONE
        private var keySize: Int? = null

        fun setBlockMode(blockMode: BlockMode): Builder = apply { this.blockMode = blockMode }
        fun setPadding(padding: Padding): Builder = apply { this.padding = padding }
        fun setKeySize(keySize: Int): Builder = apply { this.keySize = keySize }
        fun build(): KeyGeneratorSpec = KeyGeneratorSpec(
            purposes,
            blockMode,
            padding,
            keySize
        )
    }
}