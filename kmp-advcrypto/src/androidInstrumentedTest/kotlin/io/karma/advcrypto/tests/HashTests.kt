package io.karma.advcrypto.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.android.providers.DefaultCryptoProvider
import io.karma.advcrypto.wrapper.Hasher
import org.junit.Test

class HashTests {

    @Test
    fun testHashMD5() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(DefaultCryptoProvider())
        }

        val hash = Hasher.getInstance("MD5").hash("Test".encodeToByteArray())
        assert(hash == "0cbc6611f5540bd0809a388dc95a615b")
    }

    @Test
    fun testHashSHA1() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(DefaultCryptoProvider())
        }

        val hash = Hasher.getInstance("SHA1").hash("Test".encodeToByteArray())
        assert(hash == "640ab2bae07bedc4c163f679a746f7ab7fb5d1fa")
    }

    @Test
    fun testHashSHA256() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(DefaultCryptoProvider())
        }

        val hash = Hasher.getInstance("SHA256").hash("Test".encodeToByteArray())
        assert(hash == "532eaabd9574880dbf76b9b8cc00832c20a6ec113d682299550d7a6e0f345e25")
    }

    @Test
    fun testHashSHA512() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(DefaultCryptoProvider())
        }

        val hash = Hasher.getInstance("SHA512").hash("Test".encodeToByteArray())
        assert(hash == "c6ee9e33cf5c6715a1d148fd73f7318884b41adcb916021e2bc0e800a5c5dd97f5142178f6ae88c8fdd98e1afb0ce4c8d2c54b5f37b30b7da1997bb33b0b8a31")
    }

}