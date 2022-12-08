package movie.metropolis.app.screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<T>.retainStateIn(scope: CoroutineScope, initial: T) = stateIn(
    scope = scope,
    started = SharingStarted.WhileSubscribed(replayExpirationMillis = Long.MAX_VALUE),
    initialValue = initial
)