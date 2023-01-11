package movie.rating

import movie.log.log

interface LinkProvider {

    @Throws(ResultNotFoundException::class)
    suspend fun getLink(descriptor: MovieDescriptor): String

}

suspend fun LinkProvider.getLinkOrNull(descriptor: MovieDescriptor) =
    runCatching { getLink(descriptor) }.log().getOrNull()

suspend fun LinkProvider.getLinkOrNull(vararg descriptors: MovieDescriptor) = descriptors
    .distinct()
    .firstNotNullOfOrNull { getLinkOrNull(it) }