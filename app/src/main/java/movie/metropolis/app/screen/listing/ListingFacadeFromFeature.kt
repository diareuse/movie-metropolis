package movie.metropolis.app.screen.listing

import movie.core.EventFeature
import movie.core.FavoriteFeature
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewFromFeature

class ListingFacadeFromFeature(
    private val event: EventFeature,
    private val favorite: FavoriteFeature
) : ListingFacade {

    override suspend fun getCurrent(): Result<List<MovieView>> {
        val values = event.getCurrent().getOrThrow()
            .map { MovieViewFromFeature(it, favorite.isFavorite(it).getOrThrow()) }
        return Result.success(values)
    }

    override suspend fun getUpcoming(): Result<List<MovieView>> {
        val values = event.getUpcoming().getOrThrow()
            .map { MovieViewFromFeature(it, favorite.isFavorite(it).getOrThrow()) }
        return Result.success(values)
    }

}