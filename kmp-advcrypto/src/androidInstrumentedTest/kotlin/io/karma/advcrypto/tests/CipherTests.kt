package io.karma.advcrypto.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.KeyGeneratorSpec
import io.karma.advcrypto.android.providers.DefaultCryptoProvider
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.Cipher
import io.karma.advcrypto.wrapper.KeyGenerator
import org.junit.Test

class CipherTests {

    @Test
    fun testCipherAES() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(DefaultCryptoProvider())
        }

        // Generate key
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.initialize(KeyGeneratorSpec.Builder(Key.PURPOSE_SYMMETRIC).run {
            setKeySize(256)
            build()
        })
        val key = keyGenerator.generateKey()
        println(key)

        // Encrypt and decrypt
        val cipher = Cipher.getInstance("AES")
        cipher.initialize(key)
        val encryptedData = cipher.encrypt("Test".encodeToByteArray())
        val decryptedData = cipher.decrypt(encryptedData).decodeToString()
        assert(decryptedData == "Test")
    }

}