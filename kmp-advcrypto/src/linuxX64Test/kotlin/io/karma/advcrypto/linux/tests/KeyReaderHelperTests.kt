package io.karma.advcrypto.linux.tests

import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.enum.KeyFormat
import io.karma.advcrypto.keys.enum.KeyType
import io.karma.advcrypto.linux.utils.KeyReaderHelper
import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test

@OptIn(ExperimentalNativeApi::class)
class KeyReaderHelperTests {
    private val fileSystem = FileSystem.SYSTEM

    @Test
    fun testPEM() {
        fileSystem.read("./testkeys/rsa-private-key.pem".toPath()) {
            val key = KeyReaderHelper.tryParse(readByteArray(), Key.PURPOSES_ALL)!!
            close()
            assert(key.algorithm == "RSA")
            assert(key.format == KeyFormat.PEM)
            assert(key.type == KeyType.PRIVATE)
        }

        fileSystem.read("./testkeys/rsa-public-key.pem".toPath()) {
            val key = KeyReaderHelper.tryParse(readByteArray(), Key.PURPOSES_ALL)!!
            close()
            assert(key.algorithm == "RSA")
            assert(key.format == KeyFormat.PEM)
            assert(key.type == KeyType.PUBLIC)
        }
    }

    @Test
    fun testDER() {
        fileSystem.read("./testkeys/rsa-private-key.der".toPath()) {
            val key = KeyReaderHelper.tryParse(readByteArray(), Key.PURPOSES_ALL)!!
            close()
            assert(key.algorithm == "RSA")
            assert(key.format == KeyFormat.DER)
            assert(key.type == KeyType.PRIVATE)
        }

        fileSystem.read("./testkeys/rsa-public-key.der".toPath()) {
            val key = KeyReaderHelper.tryParse(readByteArray(), Key.PURPOSES_ALL)!!
            close()
            assert(key.algorithm == "RSA")
            assert(key.format == KeyFormat.DER)
            assert(key.type == KeyType.PUBLIC)
        }
    }

}