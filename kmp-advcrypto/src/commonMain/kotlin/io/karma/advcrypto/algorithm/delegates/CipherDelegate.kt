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

import io.karma.advcrypto.algorithm.specs.CipherSpec
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.Cipher

data class CipherContext<C>(val spec: CipherSpec, val key: Key, val internalContext: C)

/**
 * This class is used to implement the cipher functionality into a algorithm. This delegate is used
 * for encryption algorithms like AES or RSA. You can configure the following properties of the
 * cipher:
 * - [CipherDelegate.initializer] (required): The initializer of the cipher context
 * - [CipherDelegate.encrypt] (required): The method to encrypt specific data with the context
 * - [CipherDelegate.decrypt] (required): The method to decrypt specific data with the context
 *
 * @author Cedric Hammes
 * @since  11/06/2024
 */
class CipherDelegate<C: Any> {
    private lateinit var initializer: (CipherSpec, Key) -> CipherContext<C>
    private lateinit var encrypt: ((CipherContext<C>, ByteArray) -> ByteArray)
    private lateinit var decrypt: ((CipherContext<C>, ByteArray) -> ByteArray)

    /**
     * This method creates a new cipher object as a wrapper around the delegate functions with the
     * specified property functions.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun createCipher(): Cipher {
        return object: Cipher {
            private var context: CipherContext<C>? = null

            override fun initialize(spec: CipherSpec) {
                this.context = initializer(spec, spec.key)
            }

            override fun encrypt(data: ByteArray): ByteArray {
                if (this.context == null) {
                    throw IllegalStateException("Unable to encrypt without initialized cipher")
                }
                return encrypt.invoke(this.context!!, data)
            }

            override fun decrypt(data: ByteArray): ByteArray {
                if (this.context == null) {
                    throw IllegalStateException("Unable to decrypt without initialized cipher")
                }
                return decrypt.invoke(this.context!!, data)
            }

        }
    }

    /**
     * This method sets a delegate to the initializer function to initialize the context of a cipher
     * created.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun initializer(closure: (spec: CipherSpec, key: Key) -> C) {
        this.initializer = { spec, key ->
            if ((key.purposes and (Key.PURPOSE_ENCRYPT or Key.PURPOSE_DECRYPT)).toInt() == 0) {
                throw UnsupportedOperationException("This key doesn't support encrypt or decrypt")
            }
            CipherContext(spec, key, closure(spec, key))
        }
    }

    /**
     * This method sets a delegate to the encrypt function of the cipher for the encryption of
     * specified data with the context.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun encrypt(closure: (context: CipherContext<C>, data: ByteArray) -> ByteArray) {
        this.encrypt = { context, data ->
            if ((context.key.purposes and Key.PURPOSE_ENCRYPT) != Key.PURPOSE_ENCRYPT) {
                throw UnsupportedOperationException("This key doesn't support encrypt mode")
            }
            closure(context, data)
        }
    }

    /**
     * This method sets a delegate to the decrypt function of the cipher for the decryption of
     * specified data with the context.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun decrypt(closure: (context: CipherContext<C>, data: ByteArray) -> ByteArray) {
        this.decrypt = { context, data ->
            if ((context.key.purposes and Key.PURPOSE_DECRYPT) != Key.PURPOSE_DECRYPT) {
                throw UnsupportedOperationException("This key doesn't support decrypt mode")
            }
            closure(context, data)
        }
    }

}