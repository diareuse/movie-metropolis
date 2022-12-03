package movie.metropolis.app.screen.listing

import movie.metropolis.app.model.MovieView

class ListingFacadeRecover(
    private val origin: ListingFacade
) : ListingFacade {

    override suspend fun getCurrent(): Result<List<MovieView>> {
        return kotlin.runCatching { origin.getCurrent().getOrThrow() }
    }

    override suspend fun getUpcoming(): Result<List<MovieView>> {
        return kotlin.runCatching { origin.getUpcoming().getOrThrow() }
    }

}