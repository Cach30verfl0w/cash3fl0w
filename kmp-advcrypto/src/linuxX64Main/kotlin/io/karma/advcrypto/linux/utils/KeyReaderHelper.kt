/*
 * Copyright (c) 2024 Cach30verfl0w
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.karma.advcrypto.linux.utils

import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.enum.KeyFormat
import io.karma.advcrypto.keys.enum.KeyType
import io.karma.advcrypto.linux.keys.OpenSSLKey
import io.karma.advcrypto.linux.keys.OpenSSLPKey
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import libssl.BIO
import libssl.BIO_free
import libssl.BIO_new
import libssl.BIO_s_secmem
import libssl.BIO_write
import libssl.EVP_PKEY
import libssl.EVP_PKEY_free
import libssl.EVP_PKEY_get_bits
import libssl.PEM_read_bio_PUBKEY
import libssl.PEM_read_bio_PrivateKey
import libssl.d2i_PUBKEY_bio
import libssl.d2i_PrivateKey_bio
import platform.posix.memcpy

/**
 * This utility is used to read keys from a raw memory pointer, size and purpose. This is used in
 * the default keystore to create keys from byte arrays, strings and files. This class uses a few
 * methods to try to interpret the key with different formats. The format that doesn't fails is
 * being chosen and returned to the caller. Otherwise this helper returns null to the caller.
 *
 * This utility is 100% based on OpenSSL and it's BIO interface. We try to hold secret information
 * like private keys as secure as possible. So we try to instrument system features to hide that
 * information.
 *
 * @author Cedric Hammes
 * @since  14/06/2024
 */
@OptIn(ExperimentalForeignApi::class)
object KeyReaderHelper {

    /**
     * This method creates a BIO (Basic Input/Output) object in secured memory and writes the
     * specified data into it. This memory is more protected against memory leakage as normal
     * memory.
     *
     * This buffer is marked secure because is holds secret/private keys and the leakage of
     * them is a security concern and this memory tries to prevent that.
     *
     * @author Cedric Hammes
     * @since  14/06/2024
     */
    private fun createSecureMemoryBuffer(pointer: CPointer<ByteVar>, size: ULong): CPointer<BIO> =
        BIO_new(BIO_s_secmem()).apply {
            BIO_write(this, pointer, size.toInt())
        }?: throw RuntimeException("Error while writing key into secure memory BIO")

    /**
     * This method tries to parse the content as a PEM private key or PEM public key. If it works,
     * this method returns a pair with the key object and the type of the key.
     *
     * @author Cedric Hammes
     * @since  15/06/2024
     */
    private fun getPEMKey(pointer: CPointer<ByteVar>, size: ULong): Pair<CPointer<EVP_PKEY>, KeyType>? {
        // Try to interpret the key as private key. If it works, we return it
        val privateKeyBuffer = createSecureMemoryBuffer(pointer, size)
        val privateKey = PEM_read_bio_PrivateKey(privateKeyBuffer, null, null, null)
        BIO_free(privateKeyBuffer)
        if (privateKey != null) {
            return Pair(privateKey, KeyType.PRIVATE)
        }

        // Try to interpret the key as public key. If it works, we return it
        val publicKeyBuffer = createSecureMemoryBuffer(pointer, size)
        val publicKey = PEM_read_bio_PUBKEY(publicKeyBuffer, null, null, null)
        BIO_free(publicKeyBuffer)
        if (publicKey != null) {
            return Pair(publicKey, KeyType.PUBLIC)
        }
        return null
    }

    /**
     * This method constructs a key buffer and try to interpret it as PEM-formated private key. If
     * that doesn't work, it tries to create a new buffer and interpret it as PEM-formatted public
     * key. If one of these worked, this function creates the key from the content and returns it
     * to the caller. Otherwise this function returns null
     *
     * This method is try-to-parse and only works if the key it PEM-formatted. Otherwise we return
     * null (like mentioned before). All buffers created a being freed by the method itself and
     * the key returned is being freed because of the [AutoCloseable].
     *
     * @param pointer  Pointer to the (encoded) data to try to interpret
     * @param size     The size of the data provided by the pointer
     * @param purposes The purposes of the key or the key creation
     * @return         The key if one of the parses succeeds, otherwise null
     *
     * @author Cedric Hammes
     * @since  14/06/2024
     */
    private fun tryParseAsPEM(pointer: CPointer<ByteVar>, size: ULong, purposes: UByte): Key? {
        // Try to interpret the key as PEM-formatted key and return
        val key = getPEMKey(pointer, size)
        if (key != null) {
            return OpenSSLPKey(key.first, purposes, key.second, KeyFormat.PEM)
        }
        return null
    }

    /**
     * This method tries to parse the content as a DER private key or DER public key. If it works,
     * this method returns a pair with the key object and the type of the key.
     *
     * @author Cedric Hammes
     * @since  15/06/2024
     */
    private fun getDERKey(pointer: CPointer<ByteVar>, size: ULong):
            Pair<CPointer<EVP_PKEY>, KeyType>? {
        // Try to interpret the key as private key. If it works, we return it
        val privateKeyBuffer = createSecureMemoryBuffer(pointer, size)
        val privateKey = d2i_PrivateKey_bio(privateKeyBuffer, null)
        BIO_free(privateKeyBuffer)
        if (privateKey != null) {
            return Pair(privateKey, KeyType.PRIVATE)
        }

        // Try to interpret the key as public key. If it works, we return it
        val publicKeyBuffer = createSecureMemoryBuffer(pointer, size)
        val publicKey = d2i_PUBKEY_bio(publicKeyBuffer, null)
        BIO_free(publicKeyBuffer)
        if (publicKey != null) {
            return Pair(publicKey, KeyType.PUBLIC)
        }
        return null
    }

    /**
     * This method constructs a key buffer and try to interpret it as DER-formated private key. If
     * that doesn't work, it tries to create a new buffer and interpret it as DER-formatted public
     * key. If one of these worked, this function creates the key from the content and returns it
     * to the caller. Otherwise this function returns null
     *
     * This method is try-to-parse and only works if the key it DER-formatted. Otherwise we return
     * null (like mentioned before). All buffers created a being freed by the method itself and
     * the key returned is being freed because of the [AutoCloseable].
     *
     * @param pointer  Pointer to the (encoded) data to try to interpret
     * @param size     The size of the data provided by the pointer
     * @param purposes The purposes of the key or the key creation
     * @return         The key if one of the parses succeeds, otherwise null
     *
     * @author Cedric Hammes
     * @since  14/06/2024
     */
    private fun tryParseAsDER(pointer: CPointer<ByteVar>, size: ULong, purposes: UByte): Key? {
        // Try to interpret the key as PEM-formatted key and return
        val key = getDERKey(pointer, size)
        if (key != null) {
            return OpenSSLPKey(key.first, purposes, key.second, KeyFormat.DER)
        }
        return null
    }

    /**
     * This method tries to parse the data of the specified pointer in a key. This method supports
     * PEM and DER. If no supported format worked, this method simply returns null.
     *
     * @author Cedric Hammes
     * @since  14/06/2024
     */
    private fun tryParse(pointer: CPointer<ByteVar>, size: ULong, purposes: UByte): Key? {
        // Try to parse as PEM and return if successful
        val pemKey = tryParseAsPEM(pointer, size, purposes)
        if (pemKey != null) {
            return pemKey
        }

        // Try to parse as DER and return if successful
        val derKey = tryParseAsDER(pointer, size, purposes)
        if (derKey != null) {
            return derKey
        }

        // If no format works, return null
        return null
    }

    /**
     * This method tries to parse the data of the specified array in a key. This method supports PEM
     * and DER. If no supported format worked, this method simply returns null. If the key algorithm
     * was specified as AES, it parses a raw key.
     *
     * @author Cedric Hammes
     * @since  14/06/2024
     */
    fun tryParse(array: ByteArray, purposes: UByte, algorithm: String? = null,
                 secureHeap: SecureHeap? = null): Key? = array.usePinned {
        if (algorithm == "AES") { // TODO: Other formats than raw?
            if (secureHeap == null)
                throw IllegalArgumentException("If you are importing an AES key, please specify a secure heap")

            val bitSize = array.size * 8
            if (!arrayOf(128, 196, 256).contains(bitSize))
                throw IllegalArgumentException("The AES key file doesn't match the allowed bit sizes for the key ($bitSize)")

            val secureMemory = secureHeap.allocate(array.size.toULong())
            memcpy(secureMemory, array.refTo(0), array.size.convert())
            return OpenSSLKey(secureHeap, purposes, algorithm, secureMemory.reinterpret(),
                array.size.toULong(), KeyType.SECRET)
        }

        val parsedKey = tryParse(it.addressOf(0), array.size.toULong(), purposes)?: return null
        if (algorithm != null && parsedKey.algorithm != algorithm)
            throw IllegalArgumentException("The algorithm '$algorithm' was specified, but key is '${parsedKey.algorithm}'")
        return parsedKey
    }

    /**
     * This method tries to acquire the key's size in bits based on the type of the key. If the key
     * is a internal-provided key, this method uses the easiest method to parse the key. Below this
     * text you see a list of all supported key types and how we acquire this value from it:
     * - [OpenSSLKey]: Try to parse it into the format and acquire key's size with OpenSSL. If the
     * key is binary "formatted", return the size of the buffer multiplied with 8.
     * - [OpenSSLPKey]: Acquire the raw key object and read the size from it with the OpenSSL method
     * [EVP_PKEY_get_bits].
     * - Other: Copy the content into another secure buffer and parse it based on the format of the
     * key. It's like the method for the [OpenSSLKey] object but without the copy.
     *
     * @author Cedric Hammes
     * @since  15/06/2024
     */
    fun readKeySizeInBits(key: Key): Int {
        if (key is OpenSSLKey) {
            return when(key.format) {
                KeyFormat.PEM -> checkNotNull(getPEMKey(key.rawDataPtr.reinterpret(),
                    key.rawDataSize)).first.run {
                        val keySize = EVP_PKEY_get_bits(this)
                        EVP_PKEY_free(this)
                        return keySize
                    }
                KeyFormat.DER -> checkNotNull(getDERKey(key.rawDataPtr.reinterpret(),
                    key.rawDataSize)).first.run {
                        val keySize = EVP_PKEY_get_bits(this)
                        EVP_PKEY_free(this)
                        return keySize
                    }
                KeyFormat.BINARY -> (key.rawDataSize * 8u).toInt()
                else -> throw UnsupportedOperationException("Format ${key.format} is unsupported")
            }
        }

        if (key is OpenSSLPKey) {
            return EVP_PKEY_get_bits(key.rawKey)
        }

        TODO("Read key into secure buffer by a function like copyTo() with a secure-allocated buffer")
    }

}