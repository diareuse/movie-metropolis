package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

inline fun <T> ResultCallback<T>.then(
    scope: CoroutineScope,
    crossinline body: suspend (T) -> Unit
): ResultCallback<T> {
    var job: Job? = null
    return { result ->
        invoke(result)
        job?.cancel()
        job = scope.launch {
            result.onSuccess { body(it) }
        }
    }
}

inline fun <T> ResultCallback<T>.thenMap(
    scope: CoroutineScope,
    crossinline body: suspend (T) -> T
): ResultCallback<T> {
    var job: Job? = null
    return { result ->
        invoke(result)
        job?.cancel()
        job = scope.launch {
            invoke(result.mapCatching { body(it) })
        }
    }
}

inline fun <T> ResultCallback<T>.map(
    crossinline body: suspend (T) -> T
): ResultCallback<T> = result { result ->
    result.mapCatching { body(it) }
}

inline fun <T> ResultCallback<T>.result(
    crossinline body: suspend (Result<T>) -> Result<T>
): ResultCallback<T> {
    return { result ->
        invoke(body(result))
    }
}

inline fun <T> ResultCallback<List<T>>.thenParallelize(
    scope: CoroutineScope,
    crossinline body: suspend (T) -> T
): ResultCallback<List<T>> {
    return then(scope) {
        parallelize(scope, it, body)
    }
}

inline fun <T> ResultCallback<List<T>>.parallelize(
    scope: CoroutineScope,
    list: List<T>,
    crossinline body: suspend (T) -> T
) {
    val updated = list.toMutableList()
    for ((index, item) in list.withIndex()) scope.launch {
        updated[index] = body(item)
        invoke(Result.success(updated))
    }
}