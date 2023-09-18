package movie.rating

import movie.rating.database.RatingDao
import movie.rating.database.RatingStored
import movie.rating.internal.AvailableRating

internal class MetadataProviderDatabase(
    private val dao: RatingDao,
    domain: String
) : MetadataProvider {

    private val domain = "%$domain%"

    override suspend fun get(descriptor: MovieDescriptor): AvailableRating? {
        return dao.select(descriptor.name, descriptor.year, domain)?.let(::AvailableRating)
    }

    private fun AvailableRating(rating: RatingStored) = AvailableRating(
        value = rating.rating,
        url = rating.url
    )

}