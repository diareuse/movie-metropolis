package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import movie.log.LoggerContext
import kotlin.coroutines.CoroutineContext

val supervisorScope = CoroutineScope(SupervisorJob())

inline fun <T> ResultCallback<T>.then(
    context: CoroutineContext = Dispatchers.Default,
    crossinline body: suspend (T) -> Unit
): ResultCallback<T> {
    var job: Job? = null
    return { result ->
        invoke(result)
        result.onSuccess {
            job?.cancel()
            job = supervisorScope.launch(context = context + LoggerContext) {
                body(it)
            }
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
        result.onSuccess {
            job?.cancel()
            job = scope.launch(context = context) {
                invoke(result.mapCatching { body(it) })
            }
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
        invoke(Result.success(updated.toList()))
    }
}

inline fun <T> ResultCallback<List<T>>.parallelizeContinuous(
    scope: CoroutineScope,
    list: List<T>,
    context: CoroutineContext = Dispatchers.Default,
    crossinline body: suspend (T, callback: suspend (T) -> Unit) -> Unit
) {
    val updated = list.toMutableList()
    for ((index, item) in list.withIndex()) scope.launch(context = context) {
        body(item) {
            updated[index] = it
            invoke(Result.success(updated.toList()))
        }
    }
}

inline fun <K, V> ResultCallback<Map<K, V>>.parallelize(
    scope: CoroutineScope,
    list: Iterable<K>,
    context: CoroutineContext = Dispatchers.Default,
    crossinline body: suspend (K) -> V
) {
    val output = mutableMapOf<K, V>()
    val mutex = Mutex(false)
    for (item in list) scope.launch(context = context) {
        val snapshot = mutex.withLock {
            output[item] = body(item)
            output.toMap()
        }
        invoke(Result.success(snapshot))
    }
}

fun <T> ResultCallback<T>.collectInto(value: MutableResult<T>): ResultCallback<T> {
    return {
        value.value = it
        invoke(it)
    }
}