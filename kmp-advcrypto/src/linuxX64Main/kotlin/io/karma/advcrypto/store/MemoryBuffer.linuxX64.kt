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

@file:OptIn(ExperimentalForeignApi::class)

package io.karma.advcrypto.store

import io.karma.advcrypto.annotations.InsecureCryptoApi
import io.karma.advcrypto.linux.utils.SecureHeap
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import libssl.BIO_CTRL_INFO
import libssl.BIO_METHOD
import libssl.BIO_ctrl
import libssl.BIO_ctrl_pending
import libssl.BIO_free
import libssl.BIO_new
import libssl.BIO_read
import libssl.BIO_s_mem
import libssl.BIO_s_secmem
import libssl.BIO_write
import libssl.OPENSSL_INIT_LOAD_CONFIG
import libssl.OPENSSL_init_crypto
import platform.posix.memcpy

/**
 * This method creates a memory buffer from the specified byte array. This can be used to
 * copy data directly into the byte array
 *
 * @author Cedric Hammes
 * @since  16/06/2024
 */
actual fun emptyBuffer(): MemoryBuffer = BIOMemoryBuffer(checkNotNull(BIO_s_mem()))

/**
 * This method creates a secure memory buffer based on the platform.
 *
 * @author Cedric Hammes
 * @since  16/06/2024
 */
actual fun secureBuffer(): MemoryBuffer = BIOMemoryBuffer(checkNotNull(BIO_s_secmem()))

class PointerMemoryBuffer(
    val pointer: CPointer<*>,
    override val size: Int
): MemoryBuffer {
    /**
     * This method copies this memory buffer into another memory buffer.
     *
     * @author Cedric Hammes
     * @since  16/06/2024
     */
    override fun copyInto(buffer: MemoryBuffer, size: Int) {
        if (buffer is BIOMemoryBuffer) {
            BIO_write(buffer.bio, pointer, size)
            return
        }

        if (buffer is PointerMemoryBuffer) {
            memcpy(buffer.pointer, pointer, size.toULong())
            return
        }

        throw IllegalArgumentException("Linux-based memory buffers only supports same type")
    }

    @InsecureCryptoApi
    override fun toByteArray(): ByteArray = ByteArray(this.size).apply {
        usePinned { pinnedData ->
            memcpy(pinnedData.addressOf(0), pointer, size.toULong())
        }
    }

    override fun close() {}
}

/**
 * This is the implementation of a memory buffer. Memory buffers are used to provide cross-platform
 * copy and write operations for insecure and secure memory.
 *
 * @author Cedric Hammes
 * @since  16/06/2024
 */
class BIOMemoryBuffer(bioMethod: CPointer<BIO_METHOD>): MemoryBuffer {
    val bio = BIO_new(bioMethod)
    override val size: Int
        get() = BIO_ctrl_pending(bio).toInt()

    /**
     * This method copies this memory buffer into another memory buffer.
     *
     * @author Cedric Hammes
     * @since  16/06/2024
     */
    override fun copyInto(buffer: MemoryBuffer, size: Int) {
        // Allocate temporary secure buffer and write data from this Basic I/O into the buffer.
        // Then write the data into the other Basic I/O and into this Basic I/O.
        if (buffer is BIOMemoryBuffer) {
            @OptIn(ExperimentalStdlibApi::class)
            SecureHeap().use { secureHeap ->
                val tempBuffer = secureHeap.allocate(size.toULong())
                val bytesWrite = BIO_read(this.bio, tempBuffer, size)
                BIO_write(buffer.bio, tempBuffer, bytesWrite)
                BIO_write(this.bio, tempBuffer, bytesWrite)
                secureHeap.free(size.toULong(), tempBuffer)
            }
            return
        }

        // This method simple writes the buffer of the BIO object into the pointer buffer
        if (buffer is PointerMemoryBuffer) {
            BIO_read(this.bio, buffer.pointer, size)
            return
        }

        throw IllegalArgumentException("Linux-based memory buffers only supports same type")
    }

    /**
     * This method returns the content of the memory buffer as byte array. If you are using a secure
     * buffer, this data can be leaked into insecure memory.
     *
     * @author Cedric Hammes
     * @since  16/06/2024
     */
    @InsecureCryptoApi
    override fun toByteArray(): ByteArray = ByteArray(size).apply {
        usePinned { pinnedData ->
            BIO_read(bio, pinnedData.addressOf(0), size)
        }
    }

    override fun close() {
        BIO_free(bio)
    }

}