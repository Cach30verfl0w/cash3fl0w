package io.karma.advcrypto.linux.tests

import io.karma.advcrypto.linux.utils.SecureHeap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.pointed
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.value
import libssl.free
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class, ExperimentalStdlibApi::class)
class SecureHeapTests {

    @Test
    fun test() {
        SecureHeap(ULong.SIZE_BYTES.toULong(), 0U).use { heap ->
            val pointer = heap.allocate(8U).reinterpret<ULongVar>()
            pointer.pointed.value = 100U
            assert(pointer.pointed.value == 100UL)
            heap.free(8U, pointer)
            assert(pointer.pointed.value != 100UL)
        }
    }

}
