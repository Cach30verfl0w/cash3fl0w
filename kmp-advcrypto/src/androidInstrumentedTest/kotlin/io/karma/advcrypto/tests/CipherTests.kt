package io.karma.advcrypto.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.BlockMode
import io.karma.advcrypto.algorithm.Padding
import io.karma.advcrypto.algorithm.specs.CipherSpec
import io.karma.advcrypto.algorithm.specs.KeyGeneratorSpec
import io.karma.advcrypto.android.providers.DefaultCryptoProvider
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.Cipher
import io.karma.advcrypto.wrapper.KeyGenerator
import io.karma.advcrypto.wrapper.KeyPairGenerator
import org.junit.Test

class CipherTests {

    @Test
    fun testAESWithCBC() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(DefaultCryptoProvider())
        }

        // Generate key
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.initialize(KeyGeneratorSpec.Builder(Key.PURPOSES_SYMMETRIC).run {
            setBlockMode(BlockMode.CBC)
            setPadding(Padding.PKCS7)
            setKeySize(256)
            build()
        })
        val key = keyGenerator.generateKey()
        println(key)

        // Encrypt and decrypt
        val cipher = Cipher.getInstance("AES")
        cipher.initialize(CipherSpec.Builder(key).setBlockMode(BlockMode.CBC)
            .setPadding(Padding.PKCS7).build())
        val encryptedData = cipher.encrypt("Test".encodeToByteArray())
        val decryptedData = cipher.decrypt(encryptedData).decodeToString()
        assert(decryptedData == "Test")
    }

    @Test
    fun testAESWithGCM() {
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
        cipher.initialize(CipherSpec.Builder(key).build())
        val encryptedData = cipher.encrypt("Test".encodeToByteArray())
        val decryptedData = cipher.decrypt(encryptedData).decodeToString()
        assert(decryptedData == "Test")
    }

    @Test
    fun testRSA() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(DefaultCryptoProvider())
        }

        // Generate keypair
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(KeyGeneratorSpec.Builder(Key.PURPOSES_ALL).run {
            setPadding(Padding.PKCS1)
            setKeySize(4096)
            build()
        })
        val keypair = keyPairGenerator.generateKeyPair()
        println(keypair)

        // Encrypt and decrypt
        val encryptCipher = Cipher.getInstance("RSA")
        encryptCipher.initialize(CipherSpec.Builder(keypair.publicKey).setPadding(Padding.PKCS1).build())
        val decryptCipher = Cipher.getInstance("RSA")
        decryptCipher.initialize(CipherSpec.Builder(keypair.privateKey).setPadding(Padding.PKCS1).build())

        val encryptedData = encryptCipher.encrypt("Test".encodeToByteArray())
        val decryptedData = decryptCipher.decrypt(encryptedData).decodeToString()
        assert(decryptedData == "Test")
    }

}