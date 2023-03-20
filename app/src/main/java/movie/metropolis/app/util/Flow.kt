package movie.metropolis.app.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import movie.metropolis.app.presentation.Loadable
import kotlin.time.Duration

fun <T> Flow<T>.throttleWithTimeout(timeout: Duration) = channelFlow {
    var emissionJob: Job? = null
    collect {
        val throttle = emissionJob != null
        emissionJob?.cancel()
        emissionJob = launch {
            if (throttle) delay(timeout)
            send(it)
        }
    }
}

fun <T> Flow<T>.retainStateIn(scope: CoroutineScope, initial: T) = stateIn(
    scope = scope,
    started = SharingStarted.Lazily,
    initialValue = initial
)

fun <T> Flow<Loadable<T>>.retainStateIn(scope: CoroutineScope) = retainStateIn(
    scope = scope,
    initial = Loadable.loading()
)

inline fun <T, R> Flow<Result<T>>.mapResult(crossinline body: suspend (value: T) -> R) =
    map { result -> result.map { body(it) } }

inline fun <T> Flow<Result<T>>.flatMapResult(crossinline body: suspend (value: T) -> Flow<Result<T>>) =
    flatMapLatest { result ->
        result.fold({ body(it) }, { _ -> flowOf(result) })
    }