package movie.rating

class LinkProviderCaching(
    private val origin: LinkProvider
) : LinkProvider {

    private val cache = mutableMapOf<MovieDescriptor, String>()

    override suspend fun getLink(descriptor: MovieDescriptor) = cache.getOrPut(descriptor) {
        origin.getLink(descriptor)
    }

}