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

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import de.cacheoverflow.cashflow.CashFlowApp
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AndroidPreferencesProvider: IPreferencesProvider {
    private lateinit var preferences: SharedPreferences

    init {
        CashFlowApp.instance?.applicationContext?.apply {
            val key = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            this@AndroidPreferencesProvider.preferences = EncryptedSharedPreferences.create(
                "CashFlowPreferences",
                key,
                this.applicationContext,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    override fun writeSettings(settings: CashFlowSettings) {
        this.preferences.edit().apply {
            putString("config", Json.encodeToString(settings))
            apply()
        }
    }

    override fun readSettings(): CashFlowSettings {
        return Json.decodeFromString(this.preferences.getString("config",
            Json.encodeToString(CashFlowSettings()))!!)
    }

}