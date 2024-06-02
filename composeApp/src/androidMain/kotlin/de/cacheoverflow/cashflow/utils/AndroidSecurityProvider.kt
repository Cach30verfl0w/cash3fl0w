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

import android.app.KeyguardManager
import android.view.WindowManager.LayoutParams
import de.cacheoverflow.cashflow.MainActivity
import java.security.KeyStore

class AndroidSecurityProvider: ISecurityProvider {

    private val defaultKeyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    private var screenshotDisabled = false

    /**
     * This method toggles the policy of disabling screenshots for this app. This is used to provide
     * more security to the financial information of the user against many forms of information
     * leakage.
     *
     * @author Cedric Hammes
     * @since  02/06/2024
     */
    override fun toggleScreenshotPolicy() {
        val window = MainActivity.instance?.window
        if (window != null) {
            screenshotDisabled = !screenshotDisabled
            when (screenshotDisabled) {
                true -> window.setFlags(LayoutParams.FLAG_SECURE, LayoutParams.FLAG_SECURE)
                false -> window.clearFlags(LayoutParams.FLAG_SECURE)
            }
        }
    }

    /**
     * This method checks whether the device has authentication methods like PIN etc. This check is
     * done by the system-specific component of the application.
     *
     * @author Cedric Hammes
     * @since  02/06/2024
     */
    override fun areAuthenticationMethodsAvailable(): Boolean {
        return MainActivity.instance?.getSystemService(KeyguardManager::class.java)?.isKeyguardSecure
            ?: false
    }

    /**
     * This method returns whether the screenshot policy setting is supported or not. If not, the
     * 'disable screenshots' setting is grayed out.
     *
     * @author Cedric Hammes
     * @since  02/06/2024
     */
    override fun isScreenshotPolicySupported(): Boolean {
        return true
    }

}