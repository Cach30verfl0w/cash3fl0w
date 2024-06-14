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

package io.karma.advcrypto.android

import android.annotation.SuppressLint
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import io.karma.advcrypto.algorithm.Algorithm
import io.karma.advcrypto.algorithm.BlockMode
import io.karma.advcrypto.algorithm.delegates.KeyGeneratorDelegate
import io.karma.advcrypto.algorithm.delegates.SignatureDelegate
import io.karma.advcrypto.android.keys.AndroidKey
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.KeyPair
import io.karma.advcrypto.keys.enum.KeyType
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import javax.crypto.KeyGenerator

fun purposesToAndroid(purposes: UByte): Int {
    var value = 0
    if ((purposes and Key.PURPOSE_DECRYPT) == Key.PURPOSE_DECRYPT) {
        value = KeyProperties.PURPOSE_DECRYPT
    }

    if ((purposes and Key.PURPOSE_ENCRYPT) == Key.PURPOSE_ENCRYPT) {
        value = value.or(KeyProperties.PURPOSE_ENCRYPT)
    }

    if ((purposes and Key.PURPOSE_VERIFY) == Key.PURPOSE_VERIFY) {
        value = value.or(KeyProperties.PURPOSE_VERIFY)
    }

    if ((purposes and Key.PURPOSE_SIGNING) == Key.PURPOSE_SIGNING) {
        value = value.or(KeyProperties.PURPOSE_SIGN)
    }
    return value
}

fun KeyGeneratorDelegate<KeyPairGenerator>.androidKeyPairGenerator() {
    generateKeyPair { context ->
        val purposes = context.generatorSpec.purposes
        val keyPair = context.internalContext.generateKeyPair()
        KeyPair(
            AndroidKey(
                keyPair.public,
                purposes and Key.PURPOSE_SIGNING.inv(),
                KeyType.PUBLIC
            ),
            AndroidKey(
                keyPair.private,
                purposes and Key.PURPOSE_VERIFY.inv(),
                KeyType.PRIVATE
            )
        )
    }
}

@SuppressLint("WrongConstant")
fun KeyGeneratorDelegate<KeyPairGenerator>.androidKeyPair(defaultBlockMode: BlockMode, algorithm: String) {
    initializer { initSpec ->
        val purposes = purposesToAndroid(initSpec.purposes)
        val spec = KeyGenParameterSpec.Builder("AndroidKeyStore", purposes).run {
            setKeySize(initSpec.keySize?: defaultKeySize)
            setEncryptionPaddings(initSpec.padding.toString())
            setBlockModes((initSpec.blockMode?: defaultBlockMode).toString())
            // TODO: Auth
            build()
        }

        KeyPairGenerator.getInstance(algorithm).apply {
            initialize(spec)
        }
    }

    androidKeyPairGenerator()
}

@SuppressLint("WrongConstant")
fun KeyGeneratorDelegate<KeyGenerator>.androidKey(defaultBlockMode: BlockMode, algorithm: String) {
    initializer { initSpec ->
        val purposes = purposesToAndroid(initSpec.purposes)
        val spec = KeyGenParameterSpec.Builder("AndroidKeyStore", purposes).run {
            setKeySize(initSpec.keySize?: defaultKeySize)
            setEncryptionPaddings(initSpec.padding.toString())
            setBlockModes((initSpec.blockMode?: defaultBlockMode).toString())
            // TODO: Auth
            build()
        }

        KeyGenerator.getInstance(algorithm).apply {
            init(spec)
        }
    }

    generateKey { context ->
        AndroidKey(
            context.internalContext.generateKey(),
            context.generatorSpec.purposes,
            KeyType.SECRET
        )
    }
}

fun Algorithm.androidHasher() {
    @OptIn(ExperimentalStdlibApi::class)
    hasher<MessageDigest> {
        initialize { MessageDigest.getInstance(name) }
        hash { context, data -> context.digest(data).toHexString() }
    }
}

fun SignatureDelegate<Signature>.android(algorithm: String) {
    initialize { Signature.getInstance(algorithm) }
    initVerify { context, key -> context.initVerify((key as AndroidKey).raw as PublicKey) }
    initSign { context, key -> context.initSign((key as AndroidKey).raw as PrivateKey) }
    verify { context, signature, original ->
        context.update(original)
        context.verify(signature)
    }
    sign { context, content ->
        context.update(content)
        context.sign()
    }
}