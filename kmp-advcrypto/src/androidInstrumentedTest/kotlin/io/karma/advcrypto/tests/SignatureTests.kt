package io.karma.advcrypto.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.specs.KeyGeneratorSpec
import io.karma.advcrypto.algorithm.specs.params.DilithiumKeySpecParameter
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.KeyPairGenerator
import io.karma.advcrypto.wrapper.Signature
import org.junit.Test

class SignatureTests {

    @Test
    fun testDilithium() {
        val providers = Providers()
        val keyPairGenerator = KeyPairGenerator.getInstance(providers, "Dilithium")
        val purposes = Key.PURPOSE_VERIFY or Key.PURPOSE_SIGNING
        keyPairGenerator.initialize(KeyGeneratorSpec.Builder(purposes).run {
            setParameters(DilithiumKeySpecParameter.DILITHIUM5)
            build()
        })
        val keyPair = keyPairGenerator.generateKeyPair()

        val signatureInstance = Signature.getInstance(providers, "Dilithium")
        signatureInstance.initSign(keyPair.privateKey)
        val signature = signatureInstance.sign("Test".encodeToByteArray())
        signatureInstance.initVerify(keyPair.publicKey)
        assert(signatureInstance.verify(signature, "Test".encodeToByteArray()))
        providers.close()
    }

}