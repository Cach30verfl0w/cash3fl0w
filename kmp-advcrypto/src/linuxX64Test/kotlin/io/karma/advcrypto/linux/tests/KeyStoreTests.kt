package io.karma.advcrypto.linux.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.wrapper.KeyStore
import kotlin.test.Test

class KeyStoreTests {

    @Test
    fun testFileLoading() {
        val providers = Providers()
        KeyStore.getInstance(providers, "Default")
        providers.close()
    }

}