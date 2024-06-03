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

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.security.Key

class AndroidKey(
    private val name: String,
    private val algorithm: IKey.EnumAlgorithm,
    private val paddingUsed: Boolean,
    private val updateKey: () -> Key
): IKey {
    private val keyFlow: MutableStateFlow<Key?> = MutableStateFlow(null)

    init {
        defaultCoroutineScope.launch {
            (DI.inject<ISecurityProvider>() as AndroidSecurityProvider).isAuthenticated.collect {
                if (it) {
                    // TODO: Check if key can be invalidated through re-unlock of device
                    keyFlow.emit(updateKey())
                }
            }
        }
    }

    /**
     * This method returns the name/alias of the key. This was specified while key generation in the
     * security provider.
     *
     * @author Cedric Hammes
     * @since  03/06/2024
     */
    override fun name(): String = name

    /**
     * This method returns the algorithm the key is used for, specified while key generation in the
     * security provider.
     *
     * @author Cedric Hammes
     * @since  03/06/2024
     */
    override fun algorithm(): IKey.EnumAlgorithm = algorithm

    /**
     * This method returns whether padding was used to generate the key. This was specified while
     * key generation in the security provider.
     *
     * @author Cedric Hammes
     * @since  03/06/2024
     */
    override fun paddingUsed(): Boolean = paddingUsed

}