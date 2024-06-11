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
import io.karma.advcrypto.wrapper.Cipher

data class CipherContext<C>(val key: Key, val internalContext: C)

class CipherDelegate<C: Any> {
    private lateinit var initializer: (Key) -> CipherContext<C>
    private var encrypt: ((CipherContext<C>, ByteArray) -> ByteArray)? = null
    private var decrypt: ((CipherContext<C>, ByteArray) -> ByteArray)? = null

    fun createCipher(): Cipher {
        return object: Cipher {
            private var context: CipherContext<C>? = null

            override fun initialize(key: Key) {
                this.context = initializer(key)
            }

            override fun encrypt(data: ByteArray): ByteArray {
                if (this.context == null) {
                    throw IllegalStateException("Unable to encrypt without initialized cipher")
                }
                return encrypt!!.invoke(this.context!!, data)
            }

            override fun decrypt(data: ByteArray): ByteArray {
                if (this.context == null) {
                    throw IllegalStateException("Unable to decrypt without initialized cipher")
                }
                return decrypt!!.invoke(this.context!!, data)
            }

        }
    }

    fun initializer(closure: (key: Key) -> C) {
        this.initializer = { key ->
            if ((key.purposes and (Key.PURPOSE_ENCRYPT or Key.PURPOSE_DECRYPT)).toInt() == 0) {
                throw UnsupportedOperationException("This key doesn't support encrypt or decrypt")
            }
            CipherContext(key, closure(key))
        }
    }

    fun encrypt(closure: (context: CipherContext<C>, data: ByteArray) -> ByteArray) {
        if (this.encrypt != null) {
            throw IllegalStateException("Unable to register encrypt method multiple times")
        }
        this.encrypt = { context, data ->
            if ((context.key.purposes and Key.PURPOSE_ENCRYPT) != Key.PURPOSE_ENCRYPT) {
                throw UnsupportedOperationException("This key doesn't support encrypt mode")
            }
            closure(context, data)
        }
    }

    fun decrypt(closure: (context: CipherContext<C>, data: ByteArray) -> ByteArray) {
        if (this.decrypt != null) {
            throw IllegalStateException("Unable to register decrypt method multiple times")
        }
        this.decrypt = { context, data ->
            if ((context.key.purposes and Key.PURPOSE_DECRYPT) != Key.PURPOSE_DECRYPT) {
                throw UnsupportedOperationException("This key doesn't support decrypt mode")
            }
            closure(context, data)
        }
    }

}