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

package io.karma.advcrypto.android.providers

import io.karma.advcrypto.AbstractProvider
import io.karma.advcrypto.algorithm.specs.params.DilithiumKeySpecParameter
import io.karma.advcrypto.android.defaultKeyPairGenerator
import io.karma.advcrypto.android.keys.AndroidKey
import io.karma.advcrypto.annotations.ExperimentalCryptoApi
import io.karma.advcrypto.keys.Key
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider
import org.bouncycastle.pqc.jcajce.spec.DilithiumParameterSpec
import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec
import java.security.InvalidParameterException
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Security
import java.security.Signature

@ExperimentalCryptoApi
class PQCryptoProvider: AbstractProvider(
    "PQCrypto",
    "This provider delivers access to post-quantum algorithms",
    "1.0.0-Experimental"
) {
    init {
        if (Security.getProvider(BouncyCastlePQCProvider.PROVIDER_NAME) == null) {
            Security.addProvider(BouncyCastlePQCProvider())
        }

        algorithm("Dilithium") {
            keyGenerator(Key.PURPOSE_VERIFY or Key.PURPOSE_SIGNING) {
                initializer { spec ->
                    val keySpecParameter = when (spec.parameters as DilithiumKeySpecParameter) {
                        DilithiumKeySpecParameter.DILITHIUM2 -> DilithiumParameterSpec.dilithium2
                        DilithiumKeySpecParameter.DILITHIUM3 -> DilithiumParameterSpec.dilithium3
                        DilithiumKeySpecParameter.DILITHIUM5 -> DilithiumParameterSpec.dilithium5
                    }
                    KeyPairGenerator.getInstance("Dilithium").apply { initialize(keySpecParameter) }
                }
                generateKeyPair(::defaultKeyPairGenerator)
            }

            signature<Signature> {
                initialize { Signature.getInstance("Dilithium") }
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
        }

        algorithm("Kyber") {
            keyGenerator(
                Key.PURPOSE_ENCRYPT or Key.PURPOSE_ENCRYPT,
                arrayOf(512, 768, 1024),
                1024
            ) {
                initializer { spec ->
                    val keySpecParameter = when (spec.keySize) {
                        512 -> KyberParameterSpec.kyber512
                        768 -> KyberParameterSpec.kyber768
                        1024 -> KyberParameterSpec.kyber1024
                        else -> throw InvalidParameterException()
                    }
                    KeyPairGenerator.getInstance("Kyber").apply { initialize(keySpecParameter) }
                }
                generateKeyPair(::defaultKeyPairGenerator)
            }
        }
    }
}