/*
 * Copyright 2024 Cach30verfl0w
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

package de.cacheoverflow.cashflow.utils

import de.cacheoverflow.cashflow.utils.settings.AppSettings.EnumLanguage.*
import de.cacheoverflow.cashflow.utils.settings.AppSettings.EnumLanguage
import de.cacheoverflow.cashflow.utils.settings.PreferencesProvider

interface ITranslation {
    infix fun EnumLanguage.translatesTo(message: String)
}

object DefaultTranslation: ITranslation {
    internal val languages = HashMap<EnumLanguage, String>()
    override fun EnumLanguage.translatesTo(message: String) {
        languages[this] = message
    }
}

fun translate(closure: ITranslation.() -> Unit): String {
    val settings = DI.inject<PreferencesProvider>()
    DefaultTranslation.apply(closure)
    return DefaultTranslation.languages[settings.value.language]?: "Unable"
}

fun settings() = translate {
    DE translatesTo "Einstellungen"
    EN translatesTo "Settings"
}

fun security() = translate {
    DE translatesTo "Sicherheit"
    EN translatesTo "Security"
}

fun appearance() = translate {
    DE translatesTo "Aussehen"
    EN translatesTo "Appearance"
}

fun disableScreenshots() = translate {
    DE translatesTo "Screenshots verbieten"
    EN translatesTo "Disable screenshots"
}

fun authenticationSettings() = translate {
    DE translatesTo "Authentifizierungseinstellungen"
    EN translatesTo "Authentication settings"
}

// Main Menu
fun loadingAccounts() = translate {
    DE translatesTo "Entschlüssele und lade Konten"
    EN translatesTo "Decrypt and load accounts"
}

// Acount unlock
fun unlockAccountInfo() = translate {
    DE translatesTo "Kontoinformationen freischalten"
    EN translatesTo "Unlock account information"
}

fun unlockAccountInfoSubtitle() = translate {
    DE translatesTo "Bestätige deine Identität um deine Kontoinformationen freizuschalten"
    EN translatesTo "Confirm your identity to unlock your account information"
}

// Generic authentication messages
fun awaitingAuthentication() = translate {
    DE translatesTo "Warte auf Authentifizierung"
    EN translatesTo "Awaiting authentication"
}

fun authenticationNotPossible() = translate {
    DE translatesTo "Authentifizierung nicht möglich"
    EN translatesTo "Authentication not possible"
}

fun noAuthenticationMethodsFound() = translate {
    DE translatesTo "Keine Authentifizierungsoptionen gefunden"
    EN translatesTo "No authentication options found"
}

fun hardwareNotPresent() = translate {
    DE translatesTo "Dieses Gerät besitzt nicht die Hardware"
    EN translatesTo "This device does not have the hardware"
}

fun unknownError() = translate {
    DE translatesTo "Unbekannter Fehler"
    EN translatesTo "Unknown error"
}

// Authentication information
fun keyringNotUnlocked() = translate {
    DE translatesTo "Der Schlüsselspeicher wurde nicht entsperrt"
    EN translatesTo "The key store has not been unlocked"
}

fun keyringUnlocked() = translate {
    DE translatesTo "Der Schlüsselspeicher wurde entsperrt"
    EN translatesTo "The key store has been unlocked"
}

fun keyringNotSecured() = translate {
    DE translatesTo "Der Schlüsselspeicher ist nicht gesichert"
    EN translatesTo "The key store is not secured"
}

fun keyringSecured() = translate {
    DE translatesTo "Der Schlüsselspeicher ist gesichert"
    EN translatesTo "The key store is secured"
}

fun algorithmUsed(algorithm: String) = translate {
    DE translatesTo "Algorithmus $algorithm wird genutzt"
    EN translatesTo "Algorithm $algorithm is used"
}

fun rsaExplanation() = translate {
    DE translatesTo "RSA (Rivest–Shamir–Adleman) ist ein Kryptosystem hoher Sicherheit. Mit diesem Verfahren werden Einstellungen verschlüsselt um die Schreibbarkeit an die Authentifizierung zu koppeln aber die Lesbarkeit zu garantieren."
    EN translatesTo "RSA (Rivest-Shamir-Adleman) is a high-security cryptosystem. This system is used to encrypt settings in order to link writability to authentication but guarantee readability."
}