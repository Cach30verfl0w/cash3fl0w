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
    return DefaultTranslation.languages[settings.value.language]?: ""
}

fun home() = translate {
    DE translatesTo "Home"
    EN translatesTo "Home"
    PL translatesTo "Strona główna"
}

fun settings() = translate {
    DE translatesTo "Einstellungen"
    EN translatesTo "Settings"
    PL translatesTo "Ustawienia"
}

fun security() = translate {
    DE translatesTo "Sicherheit"
    EN translatesTo "Security"
    PL translatesTo "Bezpieczeństwo"
}

fun appearance() = translate {
    DE translatesTo "Aussehen"
    EN translatesTo "Appearance"
    PL translatesTo "Wygląd"
}

fun language() = translate {
    DE translatesTo "Sprache"
    EN translatesTo "Language"
    PL translatesTo "Język"
}

fun theme() = translate {
    DE translatesTo "Thema"
    EN translatesTo "Theme"
    PL translatesTo "Motyw"
}

fun disableScreenshots() = translate {
    DE translatesTo "Screenshots verbieten"
    EN translatesTo "Disable screenshots"
    PL translatesTo "Zakaz zrzutów ekranu"
}

fun authenticationSettings() = translate {
    DE translatesTo "Authentifizierungseinstellungen"
    EN translatesTo "Authentication settings"
    PL translatesTo "Ustawienia uwierzytelnienia"
}

// Main Menu
fun loadingAccounts() = translate {
    DE translatesTo "Entschlüssele und lade Konten"
    EN translatesTo "Decrypt and load accounts"
    PL translatesTo "Deszyfrowuj i Ładuj Konta"
}

fun unlockAccountInfo() = translate {
    DE translatesTo "Kontoinformationen freischalten"
    EN translatesTo "Unlock account information"
    PL translatesTo "Odblokowywanie informacji o koncie"
}

fun unlockAccountInfoSubtitle() = translate {
    DE translatesTo "Bestätige deine Identität um deine Kontoinformationen freizuschalten"
    EN translatesTo "Confirm your identity to unlock your account information"
    PL translatesTo "Potwierdź swoją tożsamość, aby odblokować informacje o koncie"
}

fun awaitingAuthentication() = translate {
    DE translatesTo "Warte auf Authentifizierung"
    EN translatesTo "Awaiting authentication"
    PL translatesTo "Oczekiwanie na uwierzytelnienie"
}

fun authenticationNotPossible() = translate {
    DE translatesTo "Authentifizierung nicht möglich"
    EN translatesTo "Authentication not possible"
    PL translatesTo "Uwierzytelnianie nie jest możliwe"
}

fun noAuthenticationMethodsFound() = translate {
    DE translatesTo "Keine Authentifizierungsoptionen gefunden"
    EN translatesTo "No authentication options found"
    PL translatesTo "Nie znaleziono żadnych opcji uwierzytelniania"
}

fun hardwareNotPresent() = translate {
    DE translatesTo "Dieses Gerät besitzt nicht die bennötigte Hardware"
    EN translatesTo "This device does not have the hardware"
    PL translatesTo "Urządzenie jest niekompatibilne z Aplikacją"
}

fun unknownError() = translate {
    DE translatesTo "Unbekannter Fehler"
    EN translatesTo "Unknown error"
    PL translatesTo "Nieznany błąd"
}

fun keyringNotUnlocked() = translate {
    DE translatesTo "Der Schlüsselspeicher wurde nicht entsperrt"
    EN translatesTo "The key store has not been unlocked"
    PL translatesTo "Pamięć klucza nie została odblokowana"
}

fun keyringUnlocked() = translate {
    DE translatesTo "Der Schlüsselspeicher wurde entsperrt"
    EN translatesTo "The key store has been unlocked"
    PL translatesTo "Pamięć klucza została odblokowana"
}

fun keyringNotSecured() = translate {
    DE translatesTo "Der Schlüsselspeicher ist nicht gesichert"
    EN translatesTo "The key store is not secured"
    PL translatesTo "Pamięć klucza nie jest zabezpieczona"
}

fun keyringSecured() = translate {
    DE translatesTo "Der Schlüsselspeicher ist gesichert"
    EN translatesTo "The key store is secured"
    DE translatesTo "Pamięć klucza jest zabezpieczona"
}
