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

class DesktopSecurityProvider: ISecurityProvider {

    override fun getOrCreateKey(
        name: String,
        algorithm: IKey.EnumAlgorithm,
        padding: Boolean,
        needUserAuth: Boolean,
        privateKey: Boolean
    ): IKey {
        TODO()
    }

    override fun toggleScreenshotPolicy() {
        throw UnsupportedOperationException("You can't toggle screenshot policy on Desktop")
    }

    override fun areAuthenticationMethodsAvailable(): Boolean {
        return true // Authentication like passwords are always available
    }

    override fun isBiometricAuthenticationAvailable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun wasAuthenticated(): Boolean {
        return true // TODO: Lock behind authentication
    }

    override fun isScreenshotPolicySupported(): Boolean {
        return false // Blocking screenshots for an application is not possible on desktop
    }

    override fun areScreenshotsDisallowed(): Boolean {
        return false // Disallowing screenshots is not possible
    }

}