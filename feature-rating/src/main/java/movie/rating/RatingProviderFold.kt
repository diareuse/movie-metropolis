package movie.rating

import movie.rating.internal.AvailableRating

internal class RatingProviderFold(
    private vararg val options: RatingProvider
) : RatingProvider {

    override suspend fun get(descriptor: MovieDescriptor): AvailableRating? {
        return options.firstNotNullOfOrNull { it.get(descriptor) }
    }

}