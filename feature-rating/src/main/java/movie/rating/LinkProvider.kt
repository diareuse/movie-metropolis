package movie.rating

interface LinkProvider {

    @Throws(ResultNotFoundException::class)
    suspend fun getLink(descriptor: MovieDescriptor): String

}

suspend fun LinkProvider.getLinkOrNull(descriptor: MovieDescriptor) =
    kotlin.runCatching { getLink(descriptor) }.getOrNull()