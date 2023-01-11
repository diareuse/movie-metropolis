package movie.rating

import movie.log.logCatching

interface LinkProvider {

    @Throws(ResultNotFoundException::class)
    suspend fun getLink(descriptor: MovieDescriptor): String

}

suspend fun LinkProvider.getLinkOrNull(descriptor: MovieDescriptor) =
    logCatching("link-provider") { getLink(descriptor) }.getOrNull()

suspend fun LinkProvider.getLinkOrNull(vararg descriptors: MovieDescriptor) = descriptors
    .distinct()
    .firstNotNullOfOrNull { getLinkOrNull(it) }