package movie.metropolis.app.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.channelFlow
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