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

package io.karma.advcrypto.store

import io.karma.advcrypto.annotations.InsecureCryptoApi

/**
 * This method creates a empty memory buffer based on the platform.
 *
 * @author Cedric Hammes
 * @since  16/06/2024
 */
expect fun emptyBuffer(): MemoryBuffer

/**
 * This method creates a secure memory buffer based on the platform.
 *
 * @author Cedric Hammes
 * @since  16/06/2024
 */
expect fun secureBuffer(): MemoryBuffer

/**
 * This is the implementation of a memory buffer. Memory buffers are used to provide cross-platform
 * copy and write operations for insecure and secure memory.
 *
 * @author Cedric Hammes
 * @since  16/06/2024
 */
@OptIn(ExperimentalStdlibApi::class)
interface MemoryBuffer: AutoCloseable {

    /**
     * This method copies this memory buffer into another memory buffer.
     *
     * @author Cedric Hammes
     * @since  16/06/2024
     */
    fun copyInto(buffer: MemoryBuffer, size: Int = this.size)

    /**
     * This method returns the content of the memory buffer as byte array. If you are using a secure
     * buffer, this data can be leaked into insecure memory.
     *
     * @author Cedric Hammes
     * @since  16/06/2024
     */
    @InsecureCryptoApi
    fun toByteArray(): ByteArray

    val size: Int

}
