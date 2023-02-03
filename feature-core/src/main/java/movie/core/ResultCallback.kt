package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

val count = AtomicInteger(0)
fun incrementJob() = println("running jobs: ${count.incrementAndGet()}")
fun decrementJob() = println("running jobs: ${count.decrementAndGet()}")

inline fun <T> ResultCallback<T>.then(
    scope: CoroutineScope,
    crossinline body: suspend (T) -> Unit
): ResultCallback<T> {
    var job: Job? = null
    return { result ->
        invoke(result)
        job?.cancel()
        incrementJob()
        job = scope.launch {
            result.onSuccess { body(it) }
        }.also {
            it.invokeOnCompletion { decrementJob() }
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
        incrementJob()
        job = scope.launch {
            invoke(result.mapCatching { body(it) })
        }.also {
            it.invokeOnCompletion { decrementJob() }
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
    crossinline body: suspend (T) -> T
) {
    val updated = list.toMutableList()
    for ((index, item) in list.withIndex()) scope.also { incrementJob() }.launch {
        updated[index] = body(item)
        invoke(Result.success(updated))
    }.also {
        it.invokeOnCompletion { decrementJob() }
    }
}

fun <T> ResultCallback<T>.collectInto(value: MutableResult<T>): ResultCallback<T> {
    return {
        value.value = it
        invoke(it)
    }
}