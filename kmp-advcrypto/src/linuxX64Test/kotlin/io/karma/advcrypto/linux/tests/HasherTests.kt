package io.karma.advcrypto.linux.tests

import io.karma.advcrypto.Providers
import io.karma.advcrypto.wrapper.Hasher
import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test

@OptIn(ExperimentalNativeApi::class)
class HasherTests {

    @Test
    fun testMD5() {
        val providers = Providers()
        val hasher = Hasher.getInstance(providers, "MD5")
        assert(hasher.hash("Test".encodeToByteArray()) == "0cbc6611f5540bd0809a388dc95a615b")
        hasher.close()
        providers.close()
    }

    @Test
    fun testSHA1() {
        val providers = Providers()
        val hasher = Hasher.getInstance(providers, "SHA1")
        assert(hasher.hash("Test".encodeToByteArray()) == "640ab2bae07bedc4c163f679a746f7ab7fb5d1fa")
        hasher.close()
        providers.close()
    }

    @Test
    fun testSHA224() {
        val providers = Providers()
        val hasher = Hasher.getInstance(providers, "SHA224")
        assert(hasher.hash("Test".encodeToByteArray()) == "3606346815fd4d491a92649905a40da025d8cf15f095136b19f37923")
        hasher.close()
        providers.close()
    }

    @Test
    fun testSHA256() {
        val providers = Providers()
        val hasher = Hasher.getInstance(providers, "SHA256")
        assert(hasher.hash("Test".encodeToByteArray()) ==  "532eaabd9574880dbf76b9b8cc00832c20a6ec113d682299550d7a6e0f345e25")
        hasher.close()
        providers.close()
    }

    @Test
    fun testSHA384() {
        val providers = Providers()
        val hasher = Hasher.getInstance(providers, "SHA384")
        assert(hasher.hash("Test".encodeToByteArray()) == "7b8f4654076b80eb963911f19cfad1aaf4285ed48e826f6cde1b01a79aa73fadb5446e667fc4f90417782c91270540f3")
        hasher.close()
        providers.close()
    }

    @Test
    fun testSHA512() {
        val providers = Providers()
        val hasher = Hasher.getInstance(providers, "SHA512")
        assert(hasher.hash("Test".encodeToByteArray()) == "c6ee9e33cf5c6715a1d148fd73f7318884b41adcb916021e2bc0e800a5c5dd97f5142178f6ae88c8fdd98e1afb0ce4c8d2c54b5f37b30b7da1997bb33b0b8a31")
        hasher.close()
        providers.close()
    }

}