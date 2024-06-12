package io.karma.advcrypto.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.android.providers.DefaultCryptoProvider
import io.karma.advcrypto.wrapper.Hasher
import org.junit.Test

class HashTests {

    @Test
    fun testHashSHA1() {
        if (Providers.getProviderByName("Default") == null) {
            Providers.addProvider(DefaultCryptoProvider())
        }

        val hasher = Hasher.getInstance("SHA1")
        println(hasher.hash("Test".encodeToByteArray()))
    }

}