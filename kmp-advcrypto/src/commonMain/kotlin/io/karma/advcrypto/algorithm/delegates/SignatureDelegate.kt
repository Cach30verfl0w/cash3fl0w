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

import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.Signature

class SignatureDelegate<C: Any> {
    private lateinit var initialize: () -> C
    private lateinit var initVerify: (C, Key) -> Unit
    private lateinit var initSign: (C, Key) -> Unit
    private lateinit var sign: (C, ByteArray) -> ByteArray
    private lateinit var verify: (C, ByteArray, ByteArray) -> Boolean

    fun createSignature(): Signature {
        return object: Signature {
            private val context = initialize()

            override fun initVerify(key: Key) { initVerify(context, key) }

            override fun initSign(key: Key) { initSign(context, key) }

            override fun sign(data: ByteArray): ByteArray =
                this@SignatureDelegate.sign(context, data)

            override fun verify(signature: ByteArray, original: ByteArray): Boolean =
                this@SignatureDelegate.verify(context, signature, original)

        }
    }

    fun initialize(closure: () -> C) = this.apply { initialize = closure }
    fun initVerify(closure: (C, Key) -> Unit) = this.apply { initVerify = closure }
    fun initSign(closure: (C, Key) -> Unit) = this.apply { initSign = closure }
    fun sign(closure: (C, ByteArray) -> ByteArray) = this.apply { sign = closure }
    fun verify(closure: (C, ByteArray, ByteArray) -> Boolean) = this.apply { verify = closure }
}