package de.cacheoverflow.cashflow

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import de.cacheoverflow.cashflow.ui.components.RootComponent
import de.cacheoverflow.cashflow.utils.DI
import de.cacheoverflow.cashflow.utils.IKey
import de.cacheoverflow.cashflow.utils.ISecurityProvider

class MainActivity : FragmentActivity() {
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
        val key = DI.inject<ISecurityProvider>().getOrCreateKey("EEEEEE", IKey.EnumAlgorithm.AES)
    }
}

