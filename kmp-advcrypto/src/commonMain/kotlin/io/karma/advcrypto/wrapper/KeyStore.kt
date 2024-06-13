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

package io.karma.advcrypto.wrapper

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.delegates.KeyStoreFactory

/**
 * This interface represents the implementation of a keystore. A keystore is used to store the keys
 * as secure as possible on your system and load them from files as secure as possible into secured
 * memory etc.
 *
 * @author Cedric Hammes
 * @since  13/06/2024
 */
interface KeyStore {

    companion object {
        /**
         * This method returns an instance of a keystore created by the internal architecture of
         * this library. This interface is implemented in [KeyStoreFactory] and used here.
         *
         * @author Cedric Hammes
         * @since  11/06/2024
         */
        fun getInstance(providers: Providers, name: String): KeyStore {
            return (providers.getKeyStoreByName(name)?: throw
            UnsupportedOperationException("The keystore $name doesn't exists")).createKeyStore()
        }
    }

}