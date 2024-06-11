package io.karma.advcrypto.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.KeyGeneratorSpec
import io.karma.advcrypto.android.providers.DefaultCryptoProvider
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.Cipher
import io.karma.advcrypto.wrapper.KeyGenerator
import io.karma.advcrypto.wrapper.KeyPairGenerator
import org.junit.Test

class CipherTests {

    @Test
    fun testCipherAES() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(DefaultCryptoProvider())
        }

        // Generate key
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.initialize(KeyGeneratorSpec.Builder(Key.PURPOSES_SYMMETRIC).run {
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

    @Test
    fun testCipherRSA() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(DefaultCryptoProvider())
        }

        // Generate keypair
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(KeyGeneratorSpec.Builder(Key.PURPOSES_ALL).run {
            setKeySize(4096)
            build()
        })
        val keypair = keyPairGenerator.generateKeyPair()
        println(keypair)

        // Encrypt and decrypt
        val encryptCipher = Cipher.getInstance("RSA")
        encryptCipher.initialize(keypair.publicKey)
        val decryptCipher = Cipher.getInstance("RSA")
        decryptCipher.initialize(keypair.privateKey)

        val encryptedData = encryptCipher.encrypt("Test".encodeToByteArray())
        val decryptedData = decryptCipher.decrypt(encryptedData).decodeToString()
        assert(decryptedData == "Test")
    }

}