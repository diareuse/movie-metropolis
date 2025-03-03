package movie.rating

import movie.rating.database.RatingDao
import movie.rating.database.RatingStored

internal class MetadataProviderStoring(
    private val origin: MetadataProvider,
    private val dao: RatingDao
) : MetadataProvider {

    override suspend fun get(descriptor: MovieDescriptor) = origin.get(descriptor)?.also {
        val stored = RatingStored(
            name = descriptor.name,
            year = descriptor.year,
            rating = it.rating,
            poster = it.posterImageUrl,
            overlay = it.overlayImageUrl,
            id = it.id,
            releaseDate = it.releaseDate,
            description = it.description,
            trailer = it.trailerUrl
        )
        dao.insertOrUpdate(stored)
    }

}