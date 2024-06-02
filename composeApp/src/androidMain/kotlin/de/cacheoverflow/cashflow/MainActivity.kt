package de.cacheoverflow.cashflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import de.cacheoverflow.cashflow.ui.components.RootComponent

class MainActivity : ComponentActivity() {
    companion object {
        var instance: MainActivity? = null
    }

    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        val root = retainedComponent { RootComponent(it) }
        setContent {
            App(root)
        }
    }
}

