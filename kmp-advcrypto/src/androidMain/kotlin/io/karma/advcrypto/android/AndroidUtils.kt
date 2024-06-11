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

import android.security.keystore.KeyProperties
import io.karma.advcrypto.algorithm.delegates.KeyGenContext
import io.karma.advcrypto.android.keys.AndroidKey
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.KeyPair
import java.security.KeyPairGenerator

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

fun defaultKeyPairGenerator(context: KeyGenContext<KeyPairGenerator>): KeyPair {
    val purposes = context.generatorSpec.purposes
    val keyPair = context.internalContext.generateKeyPair()
    return KeyPair(
        AndroidKey(
            keyPair.public,
            purposes and Key.PURPOSE_SIGNING.inv()
        ),
        AndroidKey(
            keyPair.private,
            purposes and Key.PURPOSE_VERIFY.inv()
        )
    )
}