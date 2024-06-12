package io.karma.advcrypto.linux.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.specs.KeyGeneratorSpec
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.linux.keys.OpenSSLKey
import io.karma.advcrypto.linux.providers.OpenSSLCryptoProvider
import io.karma.advcrypto.wrapper.KeyGenerator
import kotlin.test.Test

class KeyGeneratorTests {

    @Test
    fun testAES() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(OpenSSLCryptoProvider())
        }

        (KeyGenerator.getInstance("AES")
            .initialize(KeyGeneratorSpec.Builder(Key.PURPOSES_SYMMETRIC).setKeySize(256).build())
            .generateKey() as OpenSSLKey).close()
    }

}