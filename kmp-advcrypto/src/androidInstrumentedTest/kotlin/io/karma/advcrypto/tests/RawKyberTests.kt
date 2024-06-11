package io.karma.advcrypto.tests

import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider
import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec
import org.junit.Test
import java.security.KeyPairGenerator
import java.security.Security


class RawKyberTests {
    @Test
    fun test() {
        if (Security.getProperty(BouncyCastlePQCProvider.PROVIDER_NAME) == null) {
            Security.addProvider(BouncyCastlePQCProvider())
        }

        // Generate key
        val keyGenerator = KeyPairGenerator.getInstance("Kyber")
        keyGenerator.initialize(KyberParameterSpec.kyber1024)
        val keyPair = keyGenerator.generateKeyPair()
        assert(keyPair.public != null)
        assert(keyPair.private != null)
    }

}