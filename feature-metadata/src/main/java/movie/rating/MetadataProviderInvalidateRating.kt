package movie.rating

internal class MetadataProviderInvalidateRating(
    private val origin: MetadataProvider
) : MetadataProvider {
    override suspend fun get(descriptor: MovieDescriptor): MovieMetadata? {
        return origin.get(descriptor)?.also {
            require(it.rating > 0)
        }
    }
}