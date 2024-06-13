package io.karma.advcrypto.linux.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.specs.KeyGeneratorSpec
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.KeyGenerator
import kotlin.test.Test

class KeyGeneratorTests {

    @Test
    fun testAES() {
        val providers = Providers()
        val spec = KeyGeneratorSpec.Builder(Key.PURPOSES_SYMMETRIC).setKeySize(256).build()
        KeyGenerator.getInstance(providers, "AES").initialize(spec).generateKey().close()
        providers.close()
    }

}