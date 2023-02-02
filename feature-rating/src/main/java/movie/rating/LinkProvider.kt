package movie.rating

import movie.log.log

internal interface LinkProvider {

    @Throws(ResultNotFoundException::class)
    suspend fun getLink(descriptor: MovieDescriptor): String

}

internal suspend fun LinkProvider.getLinkOrNull(descriptor: MovieDescriptor) =
    runCatching { getLink(descriptor) }.log().getOrNull()

internal suspend fun LinkProvider.getLinkOrNull(vararg descriptors: MovieDescriptor) = descriptors
    .distinct()
    .firstNotNullOfOrNull { getLinkOrNull(it) }