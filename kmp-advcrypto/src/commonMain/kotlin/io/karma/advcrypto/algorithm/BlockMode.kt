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

package io.karma.advcrypto.algorithm

import io.karma.advcrypto.annotations.InsecureCryptoApi

/**
 * This enum class implements all block modes available through this cryptography library.
 *
 * @author Cedric Hammes
 * @since  11/06/2024
 */
enum class BlockMode {

    CBC,
    @InsecureCryptoApi
    ECB,
    CTR,
    GCM

}