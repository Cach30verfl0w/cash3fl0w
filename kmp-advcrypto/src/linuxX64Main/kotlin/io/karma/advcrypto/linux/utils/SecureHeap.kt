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

package io.karma.advcrypto.linux.utils

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import libssl.CRYPTO_secure_clear_free
import libssl.CRYPTO_secure_malloc
import libssl.CRYPTO_secure_malloc_done
import libssl.CRYPTO_secure_malloc_init
import libssl.ERR_error_string
import libssl.ERR_get_error

/**
 * This class is a high-level wrapper around the secure heap provided by OpenSSL with the
 * `openssl/crypto.h` header. This heap is used to store sensitive information like keys
 * or other secrets.
 *
 * Secure heap memory has a few protection against memory leakage. First, this memory can not be
 * swapped to the disk and is not being written into core dumps. Also, the data gets deleted while
 * freeing.
 *
 * @author Cedric Hammes
 * @since  12/06/2024
 */
@OptIn(ExperimentalForeignApi::class, ExperimentalStdlibApi::class)
class SecureHeap: AutoCloseable {

    /**
     * This constructor initializes the secure heap if no secure heap was already initialized. If
     * it's already initialized, we simply skip this step.
     *
     * @author Cedric Hammes
     * @since  12/06/2024
     */
    init {
        counter += 1
        if (counter == 1) {
            CRYPTO_secure_malloc_init(UShort.MAX_VALUE.toULong() + 1u, 0u)
        }
    }

    /**
     * This method allocates a memory section with the specified size in the secure heap. If a error
     * occurs, this function throws an exception.
     *
     * @param size The size of the memory section in bytes
     * @return     A pointer to the allocated memory
     *
     * @author Cedric Hammes
     * @since  12/06/2024
     */
    fun allocate(size: ULong): COpaquePointer {
        return CRYPTO_secure_malloc(size, this.toString(), 47) ?:
        throw Exception("Failed to allocate secure heap: ${ERR_error_string(ERR_get_error(), null)?.toKString()}")
    }

    /**
     * This method frees the allocated memory from the secure heap. This also deletes the data
     * stored in the memory section.
     *
     * @param size    The size of the memory allocated
     * @param pointer The pointer to the memory itself
     *
     * @author Cedric Hammes
     * @since  12/06/2024
     */
    fun free(size: ULong, pointer: COpaquePointer) {
        CRYPTO_secure_clear_free(pointer, size, this.toString(), 51)
    }

    /**
     * This method un-initializes the secure allocation if the security allocation was initialized
     * before/is not uninitialized.
     *
     * @author Cedric Hammes
     * @since  12/06/2024
     */
    override fun close() {
        counter -= 1
        if (counter == 0) {
            CRYPTO_secure_malloc_done()
        }
    }

    companion object {
        private var counter = 0
    }


}