/*
 * This project is licensed with GNU General Public License 2.0
 * Copyright (c) 2024 Cach30verfl0w <cach0verfl0w@gmail.com>
 */

package de.cacheoverflow.cashflow

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import de.cacheoverflow.cashflow.ui.components.RootComponent

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : FragmentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        val root = retainedComponent { RootComponent(it) }
        startKoin {
            androidContext(this@MainActivity)
            modules(sharedModule, compatibilityModule)
        }
        setContent {
            App(root)
        }
    }

    companion object {
        var instance: MainActivity? = null
    }
}
