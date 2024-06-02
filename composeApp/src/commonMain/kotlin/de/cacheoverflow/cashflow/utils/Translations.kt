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

import de.cacheoverflow.cashflow.utils.EnumLanguage.*
import org.koin.mp.KoinPlatformTools

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
    val settings = KoinPlatformTools.defaultContext().get().get<ICashFlowSettingsHolder>()
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

fun disableScreenshots() = translate {
    DE translatesTo "Screenshots verbieten"
    EN translatesTo "Disable screenshots"
}

fun unlockAccountInfo() = translate {
    DE translatesTo "Kontoinformationen freischalten"
    EN translatesTo "Unlock account information"
}

fun unlockAccountInfoSubtitle() = translate {
    DE translatesTo "Bestätige deine Identität um deine Kontoinformationen freizuschalten"
    EN translatesTo "Confirm your identity to unlock your account information"
}

fun awaitingAuthentication() = translate {
    DE translatesTo "Warte auf Authentifizierung"
    EN translatesTo "Awaiting authentication"
}