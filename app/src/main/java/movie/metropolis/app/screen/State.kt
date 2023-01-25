package movie.metropolis.app.screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<T>.retainStateIn(scope: CoroutineScope, initial: T) = stateIn(
    scope = scope,
    started = SharingStarted.Lazily,
    initialValue = initial
)

fun <T> Flow<Loadable<T>>.retainStateIn(scope: CoroutineScope) = retainStateIn(
    scope = scope,
    initial = Loadable.loading()
)