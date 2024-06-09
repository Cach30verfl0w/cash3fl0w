/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.security

interface IKey {

    fun raw(): ByteArray

    fun algorithm(): String

    fun padding(): Boolean

}