package de.cacheoverflow.cashflow

import android.app.KeyguardManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import de.cacheoverflow.cashflow.ui.components.RootComponent

class MainActivity : FragmentActivity() {
    companion object {
        var instance: MainActivity? = null
    }

    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        val root = retainedComponent { RootComponent(it) }
        val manager = getSystemService(KeyguardManager::class.java).isDeviceSecure
        setContent {
            App(root)
        }
    }
}

