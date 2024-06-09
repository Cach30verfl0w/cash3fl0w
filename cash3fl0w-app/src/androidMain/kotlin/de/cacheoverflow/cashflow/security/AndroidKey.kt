/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.security

import java.security.Key

class AndroidKey(val rawKey: Key, private val padding: Boolean): IKey {
    override fun raw(): ByteArray = rawKey.encoded
    override fun algorithm(): String = rawKey.algorithm
    override fun padding(): Boolean = padding
}