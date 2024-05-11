package movie.rating.internal

import movie.rating.MovieDescriptor
import movie.rating.ResultNotFoundException

internal interface LinkProvider {

    @Throws(ResultNotFoundException::class)
    suspend fun getLink(descriptor: MovieDescriptor): String

}

internal suspend fun LinkProvider.getLinkOrNull(descriptor: MovieDescriptor) =
    runCatching { getLink(descriptor) }.getOrNull()

internal suspend fun LinkProvider.getLinkOrNull(vararg descriptors: MovieDescriptor) = descriptors
    .distinct()
    .firstNotNullOfOrNull { getLinkOrNull(it) }