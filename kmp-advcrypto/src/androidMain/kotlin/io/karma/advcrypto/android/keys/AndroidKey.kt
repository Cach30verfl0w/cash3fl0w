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

package io.karma.advcrypto.android.keys

import io.karma.advcrypto.annotations.InsecureCryptoApi
import io.karma.advcrypto.keys.Key
import io.karma.advcrypto.keys.enum.KeyFormat
import io.karma.advcrypto.keys.enum.KeyType

typealias RawKey = java.security.Key

class AndroidKey(val raw: RawKey, override val purposes: UByte, override val type: KeyType): Key {
    override val algorithm: String = raw.algorithm
    @InsecureCryptoApi
    override val encoded: ByteArray = raw.encoded?: ByteArray(0) // TODO: Why can it be null?
    override val format: KeyFormat = KeyFormat.entries.find { it.toString() == raw.format }?:
    KeyFormat.DER

    override fun toString(): String {
        return "AndroidKey(algorithm=\"$algorithm\", purposes=$purposes, raw=$raw)"
    }

    override fun close() {}
}