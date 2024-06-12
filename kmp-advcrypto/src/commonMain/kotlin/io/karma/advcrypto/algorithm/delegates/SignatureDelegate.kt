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

/**
 * This class is used to implement the signature functionality into a algorithm. This delegate is
 * used for the signature of the content with RSA or AES. You can configure the following properties
 * of the signature:
 * - [CipherDelegate.initializer] (required): The initializer of the cipher context
 * - [SignatureDelegate.initSign] (required): Initialize the context for message signing
 * - [SignatureDelegate.initVerify] (required): Initialize the context for signature verification
 * - [SignatureDelegate.sign] (required): Sign the specified content with the context created before
 * - [SignatureDelegate.verify] (required): Verify the specified signature with the original content
 * and the context created before.
 *
 * @author Cedric Hammes
 * @since  11/06/2024
 */
class SignatureDelegate<C: Any> {
    private lateinit var initialize: () -> C
    private lateinit var initVerify: (C, Key) -> Unit
    private lateinit var initSign: (C, Key) -> Unit
    private lateinit var sign: (C, ByteArray) -> ByteArray
    private lateinit var verify: (C, ByteArray, ByteArray) -> Boolean

    /**
     * This method creates a new signature that delegates through this functions to the original
     * signature code, specified before with the parameters.
     *
     * @author Cedric Hammes
     * @sine   11/06/2024
     */
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

    /**
     * This method sets a delegate to the initialization of the general signature context. This
     * context is initialized before initializing the context for verification or signing.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun initialize(closure: () -> C) = this.apply { initialize = closure }

    /**
     * This method sets a delegate to the initialization of the context for the verification of
     * messages. For this, the specified key must be a public key with the purpose of verify
     * content.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun initVerify(closure: (C, Key) -> Unit) {
        this.initVerify = { context, key ->
            if ((key.purposes and Key.PURPOSE_VERIFY) != Key.PURPOSE_VERIFY) {
                throw UnsupportedOperationException("This key doesn't support signature verify")
            }
            closure(context, key)
        }
    }

    /**
     * This method sets a delegate to the initialization of the context for the signing of messages.
     * For this, the specified key must be a private key with the purpose of sign content.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun initSign(closure: (C, Key) -> Unit) {
        this.initSign = { context, key ->
            if ((key.purposes and Key.PURPOSE_SIGNING) != Key.PURPOSE_SIGNING) {
                throw UnsupportedOperationException("This key doesn't support signing content")
            }
            closure(context, key)
        }
    }

    /**
     * This method signs the content with the key specified in the context before (If the context
     * was initialized for signing. If not, this function returns an error)
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun sign(closure: (C, ByteArray) -> ByteArray) = this.apply { sign = closure }

    /**
     * This method verifies the signature with the key specified in the context before and the
     * original content specified in this method (If the context was initialized for verification.
     * If not, this function returns an error)
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun verify(closure: (C, ByteArray, ByteArray) -> Boolean) = this.apply { verify = closure }
}