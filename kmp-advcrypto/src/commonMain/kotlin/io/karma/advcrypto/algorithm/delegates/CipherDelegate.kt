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

class CipherDelegate<C: Any> {
    private lateinit var initializer: (Key) -> C
    private var encrypt: ((C, ByteArray) -> ByteArray)? = null
    private var decrypt: ((C, ByteArray) -> ByteArray)? = null

    fun createCipher(): Cipher {
        return object: Cipher {
            private var context: C? = null

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
        this.initializer = closure
    }

    fun encrypt(closure: (context: C, data: ByteArray) -> ByteArray) {
        if (this.encrypt != null) {
            throw IllegalStateException("Unable to register encrypt method multiple times")
        }
        this.encrypt = closure
    }

    fun decrypt(closure: (context: C, data: ByteArray) -> ByteArray) {
        if (this.decrypt != null) {
            throw IllegalStateException("Unable to register decrypt method multiple times")
        }
        this.decrypt = closure
    }

}