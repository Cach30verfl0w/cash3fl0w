package io.karma.advcrypto.linux.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.annotations.InsecureCryptoApi
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.enum.KeyFormat
import io.karma.advcrypto.keys.enum.KeyType
import io.karma.advcrypto.linux.utils.KeyReaderHelper
import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test

@OptIn(ExperimentalNativeApi::class, InsecureCryptoApi::class)
class KeyReaderHelperTests {
    private val fileSystem = FileSystem.SYSTEM
    
    @Test
    fun testPEM() {
        val providers = Providers()
        fileSystem.read("./testkeys/rsa-private-key.pem".toPath()) {
            val key = KeyReaderHelper.tryParse(readByteArray(), Key.PURPOSES_ALL)!!
            assert(key.algorithm == "RSA")
            assert(key.format == KeyFormat.PEM)
            assert(key.type == KeyType.PRIVATE)

            val key1 = KeyReaderHelper.tryParse(key.encoded(), Key.PURPOSES_ALL)!!
            assert(key1.algorithm == "RSA")
            assert(key1.format == KeyFormat.PEM)
            assert(key1.type == KeyType.PUBLIC)
        }

        fileSystem.read("./testkeys/rsa-public-key.pem".toPath()) {
            val key = KeyReaderHelper.tryParse(readByteArray(), Key.PURPOSES_ALL)!!
            close()
            assert(key.algorithm == "RSA")
            assert(key.format == KeyFormat.PEM)
            assert(key.type == KeyType.PUBLIC)

            val key1 = KeyReaderHelper.tryParse(key.encoded(), Key.PURPOSES_ALL)!!
            assert(key1.algorithm == "RSA")
            assert(key1.format == KeyFormat.PEM)
            assert(key1.type == KeyType.PUBLIC)
        }
        providers.close()
    }

    @Test
    fun testDER() {
        val providers = Providers()
        fileSystem.read("./testkeys/rsa-private-key.der".toPath()) {
            val key = KeyReaderHelper.tryParse(readByteArray(), Key.PURPOSES_ALL)!!
            close()
            assert(key.algorithm == "RSA")
            assert(key.format == KeyFormat.DER)
            assert(key.type == KeyType.PRIVATE)

            val key1 = checkNotNull(KeyReaderHelper.tryParse(key.encoded(), Key.PURPOSES_ALL))
            assert(key1.algorithm == "RSA")
            assert(key1.format == KeyFormat.DER)
            assert(key1.type == KeyType.PRIVATE)
        }

        fileSystem.read("./testkeys/rsa-public-key.der".toPath()) {
            val key = KeyReaderHelper.tryParse(readByteArray(), Key.PURPOSES_ALL)!!
            close()
            assert(key.algorithm == "RSA")
            assert(key.format == KeyFormat.DER)
            assert(key.type == KeyType.PUBLIC)

            val key1 = checkNotNull(KeyReaderHelper.tryParse(key.encoded(), Key.PURPOSES_ALL))
            assert(key1.algorithm == "RSA")
            assert(key1.format == KeyFormat.DER)
            assert(key1.type == KeyType.PUBLIC)
        }
        providers.close()
    }

}