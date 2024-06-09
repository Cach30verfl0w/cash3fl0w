/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow.security

enum class EnumAuthStatus {
    SUPPORTED,
    HARDWARE_MISSING,
    UNSUPPORTED;

    fun isUnsupported(): Boolean = this != SUPPORTED
}