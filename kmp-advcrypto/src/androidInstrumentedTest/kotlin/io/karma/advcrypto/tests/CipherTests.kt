package io.karma.advcrypto.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.algorithm.BlockMode
import io.karma.advcrypto.algorithm.Padding
import io.karma.advcrypto.algorithm.specs.CipherSpec
import io.karma.advcrypto.algorithm.specs.KeyGeneratorSpec
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.wrapper.Cipher
import io.karma.advcrypto.wrapper.KeyGenerator
import io.karma.advcrypto.wrapper.KeyPairGenerator
import org.junit.Test

class CipherTests {

    @Test
    fun testAESWithCBC() {
        val providers = Providers()
        val key = KeyGenerator.getInstance(providers, "AES").apply {
            initialize(KeyGeneratorSpec.Builder(Key.PURPOSES_SYMMETRIC).run {
                setBlockMode(BlockMode.CBC)
                setPadding(Padding.PKCS7)
                setKeySize(256)
                build()
            })
        }.generateKey()

        val cipher = Cipher.getInstance(providers, "AES")
        cipher.initialize(CipherSpec.Builder(key).setBlockMode(BlockMode.CBC)
            .setPadding(Padding.PKCS7).build())
        val encryptedData = cipher.encrypt("Test".encodeToByteArray())
        val decryptedData = cipher.decrypt(encryptedData).decodeToString()
        assert(decryptedData == "Test")
        providers.close()
    }

    @Test
    fun testAESWithGCM() {
        val providers = Providers()
        val key = KeyGenerator.getInstance(providers, "AES")
            .initialize(KeyGeneratorSpec.Builder(Key.PURPOSES_SYMMETRIC).setKeySize(256).build())
            .generateKey()

        val cipher = Cipher.getInstance(providers, "AES")
        cipher.initialize(CipherSpec.Builder(key).build())
        val encryptedData = cipher.encrypt("Test".encodeToByteArray())
        val decryptedData = cipher.decrypt(encryptedData).decodeToString()
        assert(decryptedData == "Test")
        providers.close()
    }

    @Test
    fun testRSA() {
        val providers = Providers()
        val keyPair = KeyPairGenerator.getInstance(providers, "RSA")
            .initialize(KeyGeneratorSpec.Builder(Key.PURPOSES_ALL).setKeySize(4096)
                .setPadding(Padding.PKCS1).build())
            .generateKeyPair()
        val encryptCipher = Cipher.getInstance(providers, "RSA")
        encryptCipher.initialize(CipherSpec.Builder(keyPair.publicKey).setPadding(Padding.PKCS1)
            .build())
        val decryptCipher = Cipher.getInstance(providers, "RSA")
        decryptCipher.initialize(CipherSpec.Builder(keyPair.privateKey).setPadding(Padding.PKCS1)
            .build())

        val encryptedData = encryptCipher.encrypt("Test".encodeToByteArray())
        val decryptedData = decryptCipher.decrypt(encryptedData).decodeToString()
        assert(decryptedData == "Test")
        providers.close()
    }

}