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
import io.karma.advcrypto.wrapper.KeyStore
import okio.Path

/**
 * This delegate implements the functionality of a keystore. This keystore is used to load files
 * from file or from a extra-secured storage. With this delegate you define the functions of a key
 * store.
 *
 * @param C The context of a single created keystore with information shared between all operations
 *
 * @author Cedric Hammes
 * @since  13/06/2024
 */
class KeyStoreFactory<C: Any>(val name: String) {
    private lateinit var initialize: () -> C
    private var readKeyFromFile: ((C, Path, String, UByte) -> Key)? = null

    /**
     * This method creates a new keystore with the delegate functions specified and initializes the
     * context of the keystore.
     *
     * @return The created keystore itself
     *
     * @author Cedric Hammes
     * @since  13/06/2024
     */
    fun createKeyStore(): KeyStore = object: KeyStore {
        private val context = initialize()
        
        override fun readKeyFromFile(path: Path, algorithm: String, purposes: UByte): Key =
            readKeyFromFile!!.invoke(context, path, algorithm, purposes)
    }

    /**
     * This method late-initializes the initialize delegate with the specified closure parameter.
     *
     * @param closure The initialization closure called while keystore creation
     * @author        Cedric Hammes
     * @since         13/06/2024
     */
    fun initialize(closure: () -> C) = this.apply { initialize = closure }

    /**
     * This method sets the method delegate to the `readKeyFromFile` method. This delegate is used
     * in the created cipher.
     *
     * @author Cedric Hammes
     * @since  14/06/2024
     */
    fun readKeyFromFile(closure: (C, Path, String, UByte) -> Key) = this.apply {
        if (readKeyFromFile != null)
            throw IllegalStateException("Unable to set readKeyFromFile delegate twice")
        readKeyFromFile = closure
    }

}