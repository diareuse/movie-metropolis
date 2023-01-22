package movie.rating

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

interface RatingProvider {

    suspend fun getRating(descriptor: MovieDescriptor): Byte

}

suspend fun RatingProvider.getRating(vararg descriptors: MovieDescriptor) = coroutineScope {
    descriptors
        .map { async { getRating(it) } }
        .awaitAll()
        .firstNotNullOfOrNull { rating -> rating.takeUnless { it == 0.toByte() } } ?: 0
}