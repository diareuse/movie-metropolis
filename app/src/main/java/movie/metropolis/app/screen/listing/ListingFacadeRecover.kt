package movie.metropolis.app.screen.listing

import movie.log.logCatching
import movie.log.logCatchingResult
import movie.metropolis.app.model.MovieView

class ListingFacadeRecover(
    private val origin: ListingFacade
) : ListingFacade by origin {

    override suspend fun getCurrent(): Result<List<MovieView>> {
        return origin.logCatchingResult("listing-current") { getCurrent() }
    }

    override suspend fun getUpcoming(): Result<List<MovieView>> {
        return origin.logCatchingResult("listing-upcoming") { getUpcoming() }
    }

    override suspend fun toggleFavorite(movie: MovieView) {
        origin.logCatching("listing-toggle") { toggleFavorite(movie) }
    }

}