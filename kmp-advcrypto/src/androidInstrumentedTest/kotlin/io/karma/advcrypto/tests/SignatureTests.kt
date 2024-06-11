package io.karma.advcrypto.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.specs.KeyGeneratorSpec
import io.karma.advcrypto.algorithm.specs.params.DilithiumKeySpecParameter
import io.karma.advcrypto.android.providers.PQCryptoProvider
import io.karma.advcrypto.annotations.ExperimentalCryptoApi
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.KeyPairGenerator
import io.karma.advcrypto.wrapper.Signature
import org.junit.Test

class SignatureTests {

    @Test
    @OptIn(ExperimentalCryptoApi::class)
    fun testSignatureDilithium() {
        if (Providers.getProviderByName("PQCrypto") == null) {
            Providers.addProvider(PQCryptoProvider())
        }

        val keyPairGenerator = KeyPairGenerator.getInstance("Dilithium")
        val purposes = Key.PURPOSE_VERIFY or Key.PURPOSE_SIGNING
        keyPairGenerator.initialize(KeyGeneratorSpec.Builder(purposes).run {
            setParameters(DilithiumKeySpecParameter.DILITHIUM5)
            build()
        })
        val keyPair = keyPairGenerator.generateKeyPair()

        val signatureInstance = Signature.getInstance("Dilithium")
        signatureInstance.initSign(keyPair.privateKey)
        val signature = signatureInstance.sign("Test".encodeToByteArray())
        signatureInstance.initVerify(keyPair.publicKey)
        assert(signatureInstance.verify(signature, "Test".encodeToByteArray()))
    }

}