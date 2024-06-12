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
import libssl.CRYPTO_secure_malloc_initialized
import libssl.ERR_func_error_string
import libssl.ERR_get_error
import kotlin.experimental.ExperimentalNativeApi

/**
 * This class is a high-level wrapper around the secure heap provided by OpenSSL with the
 * `openssl/crypto.h` header. This heap is used to store sensitive information like keys
 * or other secrets.
 *
 * @author Cedric Hammes
 * @since  12/06/2024
 */
@OptIn(ExperimentalForeignApi::class, ExperimentalStdlibApi::class)
class OpenSSLSecureHeap(size: ULong, minSize: ULong): AutoCloseable {
    init {
        if (CRYPTO_secure_malloc_initialized() != 1) {
            CRYPTO_secure_malloc_init(size, minSize)
        }
    }

    fun allocate(size: ULong): COpaquePointer = CRYPTO_secure_malloc(size, this.toString(), 47)
        ?: throw Exception(ERR_func_error_string(ERR_get_error())?.toKString())

    fun free(size: ULong, pointer: COpaquePointer) {
        CRYPTO_secure_clear_free(pointer, size, this.toString(), 51)
    }

    override fun close() {
        if (CRYPTO_secure_malloc_initialized() == 1) {
            CRYPTO_secure_malloc_done()
        }
    }


}