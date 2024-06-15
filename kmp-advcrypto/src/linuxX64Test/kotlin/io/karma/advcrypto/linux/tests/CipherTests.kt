package io.karma.advcrypto.linux.tests

import io.karma.advcrypto.linux.utils.SecureHeap
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import libssl.AES_BLOCK_SIZE
import libssl.EVP_CIPHER_CTX_ctrl
import libssl.EVP_CIPHER_CTX_free
import libssl.EVP_CIPHER_CTX_new
import libssl.EVP_CTRL_GCM_GET_TAG
import libssl.EVP_CTRL_GCM_SET_IVLEN
import libssl.EVP_CTRL_GCM_SET_TAG
import libssl.EVP_DecryptFinal_ex
import libssl.EVP_DecryptInit_ex
import libssl.EVP_DecryptUpdate
import libssl.EVP_EncryptFinal_ex
import libssl.EVP_EncryptInit_ex
import libssl.EVP_EncryptUpdate
import libssl.EVP_aes_256_gcm
import libssl.RAND_bytes
import kotlin.test.Test

class CipherTests {

    // TODO: call EVP_EncryptUpdate and EVP_DecryptUpdate twice (to add additional authentication
    //  data)
    @OptIn(ExperimentalForeignApi::class)
    @Test
    fun test() {
        val secureHeap = SecureHeap(8192u, 0u)
        val input = "Test".encodeToByteArray()

        // Generate random key
        val key = secureHeap.allocate(64u).reinterpret<UByteVar>()
        RAND_bytes(key, 64)

        // Generate initialization vector
        val iv = secureHeap.allocate(16u).reinterpret<UByteVar>()
        RAND_bytes(iv, 16)

        // Initialize AES-256-GCM cipher
        val encryptContext = EVP_CIPHER_CTX_new().apply {
            EVP_EncryptInit_ex(this, EVP_aes_256_gcm(), null, null, null)
            EVP_CIPHER_CTX_ctrl(this, EVP_CTRL_GCM_SET_IVLEN, 16, null)
            EVP_EncryptInit_ex(this, EVP_aes_256_gcm(), null, key, iv)
        }

        val decryptContext = EVP_CIPHER_CTX_new().apply {
            EVP_DecryptInit_ex(this, EVP_aes_256_gcm(), null, null, null)
            EVP_CIPHER_CTX_ctrl(this, EVP_CTRL_GCM_SET_IVLEN, 16, null)
            EVP_DecryptInit_ex(this, EVP_aes_256_gcm(), null, key, iv)
        }

        val ciphertext = ByteArray(input.size + AES_BLOCK_SIZE - 1)
        memScoped {
            // Encrypt data
            val ciphertextLength = alloc<IntVar>()
            ciphertext.usePinned { ct ->
                input.usePinned { ip ->
                    EVP_EncryptUpdate(encryptContext, ct.addressOf(0).reinterpret(),
                        ciphertextLength.ptr, ip.addressOf(0).reinterpret(), input.size)
                }

                val len = alloc<IntVar>()
                EVP_EncryptFinal_ex(encryptContext, ct.addressOf(ciphertextLength.value).reinterpret(), len.ptr)
                ciphertextLength.value += len.value
            }

            val tag = alloc<IntVar>()
            EVP_CIPHER_CTX_ctrl(encryptContext, EVP_CTRL_GCM_GET_TAG, 16, tag.ptr)

            // Decrypt
            val output = ByteArray(ciphertext.size)
            val outputLength = alloc<IntVar>()
            output.usePinned { ot ->
                ciphertext.usePinned { ct ->
                    EVP_DecryptUpdate(decryptContext, ot.addressOf(0).reinterpret(), outputLength.ptr, ct.addressOf(0).reinterpret(), ciphertextLength.value)
                }
                EVP_CIPHER_CTX_ctrl(decryptContext, EVP_CTRL_GCM_SET_TAG, 16, tag.ptr)

                val len = alloc<IntVar>()
                EVP_DecryptFinal_ex(decryptContext, ot.addressOf(outputLength.value).reinterpret(), len.ptr)
                outputLength.value += len.value
            }
            println("EEEEE: " + output.toKString())
        }

        // Free
        EVP_CIPHER_CTX_free(encryptContext)
        EVP_CIPHER_CTX_free(decryptContext)
        secureHeap.free(16u, iv)
        secureHeap.free(64u, key)
        secureHeap.close()
    }

}