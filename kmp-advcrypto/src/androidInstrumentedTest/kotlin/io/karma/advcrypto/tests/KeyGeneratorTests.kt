package io.karma.advcrypto.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.Padding
import io.karma.advcrypto.algorithm.specs.KeyGeneratorSpec
import io.karma.advcrypto.android.providers.DefaultCryptoProvider
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.KeyGenerator
import io.karma.advcrypto.wrapper.KeyPairGenerator
import org.junit.Test

class KeyGeneratorTests {
    @Test
    fun testKeyGeneratorRSA() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(DefaultCryptoProvider())
        }

        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(KeyGeneratorSpec.Builder(Key.PURPOSES_ALL).run {
            setPadding(Padding.PKCS1)
            setKeySize(4096)
            build()
        })
        println(keyPairGenerator.generateKeyPair())
    }

    @Test
    fun testKeyGeneratorAES() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(DefaultCryptoProvider())
        }

        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.initialize(KeyGeneratorSpec.Builder(Key.PURPOSES_SYMMETRIC).run {
            setKeySize(256)
            build()
        })
        println(keyGenerator.generateKey())
    }
}