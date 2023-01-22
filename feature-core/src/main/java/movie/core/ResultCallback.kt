package movie.core

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

inline fun <T> ResultCallback<T>.then(
    crossinline body: suspend (T) -> Unit
): ResultCallback<T> {
    var job: Job? = null
    return { result ->
        invoke(result)
        coroutineScope {
            job?.cancel()
            job = launch {
                result.onSuccess { body(it) }
            }
        }
    }
}

inline fun <T> ResultCallback<T>.thenMap(
    crossinline body: suspend (T) -> T
): ResultCallback<T> {
    var job: Job? = null
    return { result ->
        invoke(result)
        coroutineScope {
            job?.cancel()
            job = launch {
                invoke(result.mapCatching { body(it) })
            }
        }
    }
}

inline fun <T> ResultCallback<T>.map(
    crossinline body: suspend (T) -> T
): ResultCallback<T> {
    return { result ->
        invoke(result.mapCatching { body(it) })
    }
}
