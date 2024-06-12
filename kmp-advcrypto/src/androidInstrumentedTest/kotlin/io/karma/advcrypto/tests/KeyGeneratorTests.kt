package io.karma.advcrypto.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.Padding
import io.karma.advcrypto.algorithm.specs.KeyGeneratorSpec
import io.karma.advcrypto.algorithm.specs.params.DilithiumKeySpecParameter
import io.karma.advcrypto.android.providers.DefaultCryptoProvider
import io.karma.advcrypto.android.providers.PQCryptoProvider
import io.karma.advcrypto.annotations.ExperimentalCryptoApi
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.KeyGenerator
import io.karma.advcrypto.wrapper.KeyPairGenerator
import org.junit.Test

class KeyGeneratorTests {

    @Test
    fun testRSA() {
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
    fun testAES() {
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
    
    @Test
    @OptIn(ExperimentalCryptoApi::class)
    fun testDilithium() {
        if (Providers.getProviderByName("PQCrypto") == null) {
            Providers.addProvider(PQCryptoProvider())
        }

        val keyPairGenerator = KeyPairGenerator.getInstance("Dilithium")
        val purposes = Key.PURPOSE_VERIFY or Key.PURPOSE_SIGNING
        keyPairGenerator.initialize(KeyGeneratorSpec.Builder(purposes).run {
            setParameters(DilithiumKeySpecParameter.DILITHIUM5)
            build()
        })
        println(keyPairGenerator.generateKeyPair())
    }

    @Test
    @OptIn(ExperimentalCryptoApi::class)
    fun testKyber() {
        if (Providers.getProviderByName("PQCrypto") == null) {
            Providers.addProvider(PQCryptoProvider())
        }

        val keyPairGenerator = KeyPairGenerator.getInstance("Kyber")
        val purposes = Key.PURPOSE_DECRYPT or Key.PURPOSE_DECRYPT
        keyPairGenerator.initialize(KeyGeneratorSpec.Builder(purposes).run {
            setKeySize(1024)
            build()
        })
        println(keyPairGenerator.generateKeyPair())
    }

}