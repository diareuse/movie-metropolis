package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

inline fun <T> ResultCallback<T>.then(
    scope: CoroutineScope,
    context: CoroutineContext = Dispatchers.Default,
    crossinline body: suspend (T) -> Unit
): ResultCallback<T> {
    var job: Job? = null
    return { result ->
        invoke(result)
        job?.cancel()
        job = scope.launch(context = context) {
            result.onSuccess { body(it) }
        }
    }
}

inline fun <T> ResultCallback<T>.thenMap(
    scope: CoroutineScope,
    context: CoroutineContext = Dispatchers.Default,
    crossinline body: suspend (T) -> T
): ResultCallback<T> {
    var job: Job? = null
    return { result ->
        invoke(result)
        job?.cancel()
        job = scope.launch(context = context) {
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

inline fun <T> ResultCallback<List<T>>.parallelize(
    scope: CoroutineScope,
    list: List<T>,
    context: CoroutineContext = Dispatchers.Default,
    crossinline body: suspend (T) -> T
) {
    val updated = list.toMutableList()
    for ((index, item) in list.withIndex()) scope.launch(context = context) {
        updated[index] = body(item)
        invoke(Result.success(updated))
    }
}

fun <T> ResultCallback<T>.collectInto(value: MutableResult<T>): ResultCallback<T> {
    return {
        value.value = it
        invoke(it)
    }
}