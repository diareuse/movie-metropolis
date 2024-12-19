package movie.metropolis.app.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import movie.metropolis.app.presentation.Loadable
import java.net.UnknownHostException
import kotlin.time.Duration

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.onEachLaunch(body: suspend CoroutineScope.(T) -> Unit) = flatMapLatest {
    flow {
        emit(it)
        coroutineScope { body(it) }
    }
}

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
    map { result -> result.mapCatching { body(it) } }

@OptIn(ExperimentalCoroutinesApi::class)
inline fun <T> Flow<Result<T>>.flatMapResult(crossinline body: suspend (value: T) -> Flow<Result<T>>) =
    flatMapLatest { result ->
        result.fold({ body(it) }, { _ -> flowOf(result) })
    }

fun <T> Flow<T>.retryOnNetworkError(
    debounce: suspend (attempt: Long) -> Unit = { delay(it * 1000) }
) = retryWhen { cause, attempt ->
    if (cause is UnknownHostException) {
        debounce(attempt)
        true
    } else {
        false
    }
}