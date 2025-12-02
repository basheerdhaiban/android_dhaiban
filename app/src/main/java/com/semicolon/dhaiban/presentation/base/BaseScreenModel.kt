package com.semicolon.dhaiban.presentation.base

import android.util.Log
import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent

@Stable
abstract class BaseScreenModel<S, E>(initialState: S) : ScreenModel/*, KoinComponent*/ {

    abstract val viewModelScope: CoroutineScope
    protected val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<E?>()
    val effect = _effect.asSharedFlow().throttleFirst(700).mapNotNull { it }

    protected fun <T> tryToExecute(
        function: suspend () -> T,
        onSuccess: (T) -> Unit,
        onError: (e: Exception) -> Unit,
        inScope: CoroutineScope = viewModelScope,
    ): Job {

        return runWithErrorCheck(onError, inScope) {
            val result = function()
            Log.d("BaseScreenModel","${result.toString()}")
            onSuccess(result)
        }
    }

    protected fun <T> tryToCollect(
        function: suspend () -> Flow<T>,
        onNewValue: (T) -> Unit,
        onError: (Exception) -> Unit,
        inScope: CoroutineScope = viewModelScope,
    ): Job {
        return runWithErrorCheck(onError, inScope) {
            function().distinctUntilChanged().collectLatest {
                delay(200)
                onNewValue(it)
            }
        }
    }

    protected fun updateState(updater: (S) -> S) {
        _state.update(updater)
    }

    protected fun sendNewEffect(newEffect: E) {
        viewModelScope.launch(Dispatchers.IO) {
            _effect.emit(newEffect)
        }
    }

    private fun runWithErrorCheck(
        onError: (Exception) -> Unit,
        inScope: CoroutineScope = viewModelScope,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        function: suspend () -> Unit,
    ): Job {
        return inScope.async (dispatcher) {
            try {
                function()
            } catch (exception: Exception) {
                onError(exception)
            }
        }
    }

    private fun <T> Flow<T>.throttleFirst(periodMillis: Long): Flow<T> {
        require(periodMillis > 0)
        return flow {
            var lastTime = 0L
            collect { value ->
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastTime >= periodMillis) {
                    lastTime = currentTime
                    emit(value)
                }
            }
        }
    }

    protected fun launchDelayed(duration: Long, block: suspend CoroutineScope.() -> Unit): Job {
        return screenModelScope.launch(Dispatchers.IO) {
            delay(duration)
            block()
        }
    }
}