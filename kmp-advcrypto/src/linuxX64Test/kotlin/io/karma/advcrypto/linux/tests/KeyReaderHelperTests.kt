package io.karma.advcrypto.linux.tests

import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.enum.KeyFormat
import io.karma.advcrypto.keys.enum.KeyType
import io.karma.advcrypto.linux.utils.KeyReaderHelper
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
class KeyReaderHelperTests {
    private val fileSystem = FileSystem.SYSTEM

    @Test
    fun testPEM() {
        fileSystem.read("./testkeys/rsa-private-key.pem".toPath()) {
            val byteArray = readByteArray()
            val size = byteArray.size.toULong()
            byteArray.usePinned { array ->
                val key = KeyReaderHelper.tryParse(array.addressOf(0), size, Key.PURPOSES_ALL)!!
                assert(key.algorithm == "RSA")
                assert(key.format == KeyFormat.PEM)
                assert(key.type == KeyType.PRIVATE)
            }
            close()
        }

        fileSystem.read("./testkeys/rsa-public-key.pem".toPath()) {
            val byteArray = readByteArray()
            val size = byteArray.size.toULong()
            byteArray.usePinned { array ->
                val key = KeyReaderHelper.tryParse(array.addressOf(0), size, Key.PURPOSES_ALL)!!
                assert(key.algorithm == "RSA")
                assert(key.format == KeyFormat.PEM)
                assert(key.type == KeyType.PUBLIC)
            }
            close()
        }
    }

    @Test
    fun testDER() {
        fileSystem.read("./testkeys/rsa-private-key.der".toPath()) {
            val byteArray = readByteArray()
            val size = byteArray.size.toULong()
            byteArray.usePinned { array ->
                val key = KeyReaderHelper.tryParse(array.addressOf(0), size, Key.PURPOSES_ALL)!!
                assert(key.algorithm == "RSA")
                assert(key.format == KeyFormat.DER)
                assert(key.type == KeyType.PRIVATE)
            }
            close()
        }

        fileSystem.read("./testkeys/rsa-public-key.der".toPath()) {
            val byteArray = readByteArray()
            val size = byteArray.size.toULong()
            byteArray.usePinned { array ->
                val key = KeyReaderHelper.tryParse(array.addressOf(0), size, Key.PURPOSES_ALL)!!
                assert(key.algorithm == "RSA")
                assert(key.format == KeyFormat.DER)
                assert(key.type == KeyType.PUBLIC)
            }
            close()
        }
    }

}