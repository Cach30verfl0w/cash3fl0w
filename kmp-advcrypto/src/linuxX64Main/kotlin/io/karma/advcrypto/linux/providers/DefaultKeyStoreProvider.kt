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

package io.karma.advcrypto.linux.providers

import io.karma.advcrypto.AbstractProvider
import io.karma.advcrypto.Providers

class DefaultKeyStoreProvider: AbstractProvider(
    "Default KeyStore",
    "This provider provides access to the keystore interface on Linux devices",
    "1.0.0-Dev"
) {
    override fun initialize(providers: Providers) {
        keyStore("Default") {
            initialize {
                "Placeholder"
            }
            readKeyFromFile { context, path ->
                TODO("Read and derive key")
            }
        }
    }

    override fun close() {}
}