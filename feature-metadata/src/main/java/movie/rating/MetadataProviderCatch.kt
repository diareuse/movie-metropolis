package movie.rating

class MetadataProviderCatch(
    private val origin: MetadataProvider
) : MetadataProvider {

    override suspend fun get(descriptor: MovieDescriptor) = try {
        origin.get(descriptor)
    } catch (ignore: Throwable) {
        null
    }

}