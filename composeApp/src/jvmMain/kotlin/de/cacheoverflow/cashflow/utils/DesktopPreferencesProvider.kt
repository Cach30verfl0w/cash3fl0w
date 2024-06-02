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

import org.lighthousegames.logging.KmLog
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.div

class DesktopPreferencesProvider: IPreferencesProvider {
    private val path: Path = Path.of(System.getProperty("user.home")) / ".Cash3Fl0w"

    init {
        if (!Files.exists(path)) {
            injectKoin<KmLog>().warn { "Application directory not found, creating it..." }
            Files.createDirectory(path)
        }
    }

    override fun writeSettings(settings: CashFlowSettings) {
    }

    override fun readSettings(): CashFlowSettings {
        return CashFlowSettings()
    }

}