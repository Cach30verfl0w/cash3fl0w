package io.karma.advcrypto.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.KeyGeneratorSpec
import io.karma.advcrypto.android.providers.RSACryptoProvider
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.KeyPairGenerator
import org.junit.Test

class RSATests {
    @Test
    fun testRSA() {
        if (Providers.getProviderByName("RSA") == null) {
            Providers.addProvider(RSACryptoProvider())
        }

        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")!!
        keyPairGenerator.initialize(KeyGeneratorSpec.Builder(Key.PURPOSE_ALL).run {
            setKeySize(4096)
            build()
        })
        val keyPair = keyPairGenerator.generateKeyPair()
        println(keyPair)
    }
}