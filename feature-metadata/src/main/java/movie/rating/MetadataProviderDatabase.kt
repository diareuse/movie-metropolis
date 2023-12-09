package movie.rating

import movie.rating.database.RatingDao
import movie.rating.database.RatingStored

internal class MetadataProviderDatabase(
    private val dao: RatingDao
) : MetadataProvider {

    override suspend fun get(descriptor: MovieDescriptor): MovieMetadata? {
        return dao.select(descriptor.name, descriptor.year)?.let(::MovieMetadata)
    }

    private fun MovieMetadata(rating: RatingStored) = MovieMetadata(
        id = rating.id,
        rating = rating.rating,
        posterImageUrl = rating.poster,
        overlayImageUrl = rating.overlay,
        url = TMDB.url("/movie/${rating.id}")
    )

}