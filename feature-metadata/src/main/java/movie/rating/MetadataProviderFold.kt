package movie.rating

import movie.rating.internal.AvailableRating

internal class MetadataProviderFold(
    private vararg val options: MetadataProvider
) : MetadataProvider {

    override suspend fun get(descriptor: MovieDescriptor): AvailableRating? {
        return options.firstNotNullOfOrNull { it.get(descriptor) }
    }

}