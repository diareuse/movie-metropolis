package movie.metropolis.app.screen.listing

import movie.log.flatMapCatching
import movie.log.logSevere
import movie.metropolis.app.model.MovieView

class ListingFacadeRecover(
    private val origin: ListingFacade
) : ListingFacade by origin {

    override suspend fun getCurrent(): Result<List<MovieView>> {
        return origin.flatMapCatching { getCurrent() }.logSevere()
    }

    override suspend fun getUpcoming(): Result<List<MovieView>> {
        return origin.flatMapCatching { getUpcoming() }.logSevere()
    }

    override suspend fun toggleFavorite(movie: MovieView) {
        origin.runCatching { toggleFavorite(movie) }.logSevere()
    }

}