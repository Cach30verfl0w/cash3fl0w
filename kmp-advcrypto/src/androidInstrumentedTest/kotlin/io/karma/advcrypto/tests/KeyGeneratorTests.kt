package io.karma.advcrypto.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.Padding
import io.karma.advcrypto.algorithm.specs.KeyGeneratorSpec
import io.karma.advcrypto.algorithm.specs.params.DilithiumKeySpecParameter
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.KeyGenerator
import io.karma.advcrypto.wrapper.KeyPairGenerator
import org.junit.Test

class KeyGeneratorTests {

    @Test
    fun testRSA() {
        val providers = Providers()
        val spec = KeyGeneratorSpec.Builder(Key.PURPOSES_ALL).setKeySize(4096)
            .setPadding(Padding.PKCS1).build()
        KeyPairGenerator.getInstance(providers, "RSA").initialize(spec).generateKeyPair().close()
        providers.close()
    }

    @Test
    fun testAES() {
        val providers = Providers()
        val spec = KeyGeneratorSpec.Builder(Key.PURPOSES_SYMMETRIC).setKeySize(256).build()
        KeyGenerator.getInstance(providers, "AES").initialize(spec).generateKey().close()
        providers.close()
    }

    @Test
    fun testDilithium() {
        val providers = Providers()
        val spec = KeyGeneratorSpec.Builder(Key.PURPOSE_VERIFY or Key.PURPOSE_SIGNING)
            .setParameters(DilithiumKeySpecParameter.DILITHIUM5).build()
        KeyPairGenerator.getInstance(providers, "Dilithium").initialize(spec).generateKeyPair()
            .close()
        providers.close()
    }

    @Test
    fun testKyber() {
        val providers = Providers()
        val spec = KeyGeneratorSpec.Builder(Key.PURPOSES_SYMMETRIC).setKeySize(1024).build()
        KeyPairGenerator.getInstance(providers, "Kyber").initialize(spec).generateKeyPair()
            .close()
        providers.close()
    }

}