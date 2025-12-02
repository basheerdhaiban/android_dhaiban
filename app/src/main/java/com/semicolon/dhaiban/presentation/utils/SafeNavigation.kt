package com.semicolon.dhaiban.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.delay

/**
 * A composable that provides safe navigation by checking the lifecycle state
 * before performing navigation actions. This prevents AndroidScreenLifecycleOwner
 * lifecycle state corruption issues.
 */
@Composable
fun SafeNavigationEffect(
    navigator: Navigator,
    navigationAction: (() -> Unit)? = null
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var isActive by remember { mutableStateOf(true) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            isActive = when (event) {
                Lifecycle.Event.ON_CREATE,
                Lifecycle.Event.ON_START,
                Lifecycle.Event.ON_RESUME -> true
                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_STOP,
                Lifecycle.Event.ON_DESTROY -> false
                else -> isActive
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(navigationAction, isActive) {
        if (navigationAction != null && isActive && 
            lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            // Add a small delay to ensure the composition is stable
            delay(50)
            if (isActive && lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                try {
                    navigationAction()
                } catch (e: IllegalStateException) {
                    // Log and ignore lifecycle-related navigation errors
                    android.util.Log.w("SafeNavigation", "Navigation action skipped due to lifecycle state", e)
                }
            }
        }
    }
}

/**
 * Safe navigation functions that check lifecycle state before navigation
 */
object SafeNavigator {
    
    fun safeReplaceAll(navigator: Navigator, screen: Screen, lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            try {
                navigator.replaceAll(screen)
            } catch (e: IllegalStateException) {
                android.util.Log.w("SafeNavigator", "replaceAll failed due to lifecycle state", e)
            }
        }
    }
    
    fun safePush(navigator: Navigator, screen: Screen, lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            try {
                navigator.push(screen)
            } catch (e: IllegalStateException) {
                android.util.Log.w("SafeNavigator", "push failed due to lifecycle state", e)
            }
        }
    }
    
    fun safePop(navigator: Navigator, lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            try {
                if (navigator.canPop) navigator.pop()
            } catch (e: IllegalStateException) {
                android.util.Log.w("SafeNavigator", "pop failed due to lifecycle state", e)
            }
        }
    }
    
    fun safeReplace(navigator: Navigator, screen: Screen, lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            try {
                navigator.replace(screen)
            } catch (e: IllegalStateException) {
                android.util.Log.w("SafeNavigator", "replace failed due to lifecycle state", e)
            }
        }
    }
}
