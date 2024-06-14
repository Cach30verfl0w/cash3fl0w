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

package io.karma.advcrypto.keys.enum

/**
 * This enum represents all types of keys. These types are available though the key interface and
 * it's implementations.
 *
 * @author Cedric Hammes
 * @since  14/06/2024
 */
enum class KeyType {

    /**
     * This enum value represents a private key type. Private keys are used in signature and
     * asymmetric encryption algorithms as a secret hold by the issuer of the key.
     *
     * @author Cedric Hammes
     * @since  14/06/2024
     */
    PRIVATE,

    /**
     * This enum value represents a public key type. Public keys are used in signature and
     * asymmetric encryption algorithms as public value hold by the issuer and any peer.
     *
     * @author Cedric Hammes
     * @since  14/06/2024
     */
    PUBLIC,

    /**
     * This enum value represents a secret key type. Secret keys are used in symmetric encryption
     * algorithms as a secret hold by both issuer and peer.
     *
     * @author Cedric Hammes
     * @since  14/06/2024
     */
    SECRET

}