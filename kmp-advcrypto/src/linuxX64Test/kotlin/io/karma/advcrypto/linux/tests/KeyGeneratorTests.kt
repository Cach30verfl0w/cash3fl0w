package io.karma.advcrypto.linux.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.specs.KeyGeneratorSpec
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.KeyGenerator
import io.karma.advcrypto.wrapper.KeyPairGenerator
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test

@OptIn(ExperimentalStdlibApi::class, ExperimentalNativeApi::class)
class KeyGeneratorTests {

    @Test
    fun testAES() {
        val providers = Providers()
        val spec = KeyGeneratorSpec.Builder(Key.PURPOSES_SYMMETRIC).setKeySize(256).build()
        KeyGenerator.getInstance(providers, "AES").initialize(spec).generateKey().use { key ->
            assert(key.algorithm == "AES")
            assert(key.purposes == Key.PURPOSES_SYMMETRIC)
        }
        providers.close()
    }

    @Test
    fun testRSA() {
        val providers = Providers()
        val spec = KeyGeneratorSpec.Builder(Key.PURPOSES_ALL).setKeySize(4096).build()
        KeyPairGenerator.getInstance(providers, "RSA").initialize(spec).generateKeyPair().use { pair ->
            assert(pair.privateKey.algorithm == "RSA")
            assert(pair.privateKey.purposes == (Key.PURPOSE_SIGNING or Key.PURPOSE_DECRYPT))

            assert(pair.publicKey.algorithm == "RSA")
            assert(pair.publicKey.purposes == (Key.PURPOSE_VERIFY or Key.PURPOSE_ENCRYPT))
        }
        providers.close()
    }

}