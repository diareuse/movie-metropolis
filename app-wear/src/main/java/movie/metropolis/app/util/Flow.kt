package movie.metropolis.app.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.presentation.Loadable

fun <T> Flow<T>.retainStateIn(scope: CoroutineScope, initial: T) = stateIn(
    scope = scope,
    started = SharingStarted.Lazily,
    initialValue = initial
)

fun <T> Flow<Loadable<T>>.retainStateIn(scope: CoroutineScope) = retainStateIn(
    scope = scope,
    initial = Loadable.loading()
)