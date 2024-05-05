package movie.cinema.city

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend inline fun <T, R> List<T>.parallelMap(
    crossinline body: suspend CoroutineScope.(T) -> R
) = coroutineScope {
    map { async { body(it) } }.awaitAll()
}
