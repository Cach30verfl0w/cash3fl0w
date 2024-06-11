/*
 * Copyright (c) 2024 Cach30verfl0w
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

package io.karma.advcrypto

import io.karma.advcrypto.algorithm.Algorithm

/**
 * This class is the implementation template of a single provider in this library. These providers
 * are used to register algorithms, key generators etc. into the system. The initialize method is
 * used for registration.
 *
 * @param name        The name of the provider
 * @param description The description of the provider
 * @param version     The version of the provider (by default 1.0.0)
 *
 * @author Cedric Hammes
 * @since  08/06/2024
 */
abstract class AbstractProvider(
    val name: String,
    val description: String,
    val version: String = "1.0.0"
) {
    private var algorithms: MutableList<Algorithm> = ArrayList()

    /**
     * This method registers the specified algorithm into this provider. The factory lambda is used
     * to define the algorithm's properties, key generators etc.
     *
     * @param name     The name of the algorithm (without padding and block mode)
     * @param factory  The factory lambda that registers key gens etc. to the algorithm
     *
     * @author Cedric Hammes
     * @since  08/06/2024
     */
    fun algorithm(name: String, factory: Algorithm.() -> Unit) {
        if (algorithms.find { it.name == name } != null) { // TODO: Change from provider to central
            throw IllegalStateException("Unable to register same algorithm twice")
        }
        this.algorithms.add(Algorithm(name).apply(factory))
    }

    /**
     * This method initializes the provider. This is called while the registration of the provider
     * into the crypto system.
     *
     * @author Cedric Hammes
     * @since  08/06/2024
     */
    abstract fun initialize()

    /**
     * This method returns a copy of the algorithms registered in this provider.
     *
     * @author Cedric Hammes
     * @since  11/06/2024
     */
    fun getAlgorithms(): List<Algorithm> = ArrayList(this.algorithms)

}