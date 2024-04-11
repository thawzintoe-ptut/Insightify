package com.ptut.insightify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ptut.insightify.navigation.SetupNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = false
                )
            }
            BackHandler {
                this.finish()
            }
            val navController = rememberNavController()
            SetupNavGraph(navController = navController)
        }
    }
}

@Composable
fun BackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
) {
    val currentOnBack by rememberUpdatedState(onBack)
    val backCallback =
        remember {
            object : OnBackPressedCallback(enabled) {
                override fun handleOnBackPressed() {
                    currentOnBack()
                }
            }
        }
    SideEffect {
        backCallback.isEnabled = enabled
    }
    val backDispatcher =
        checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
            "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
        }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}
