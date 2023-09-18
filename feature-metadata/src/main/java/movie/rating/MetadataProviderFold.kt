package movie.rating


internal class MetadataProviderFold(
    private vararg val options: MetadataProvider
) : MetadataProvider {

    override suspend fun get(descriptor: MovieDescriptor): MovieMetadata? {
        return options.firstNotNullOfOrNull { it.get(descriptor) }
    }

}