package movie.metropolis.app.presentation.listing

import movie.core.Recoverable
import movie.core.wrap
import movie.log.logSevere
import movie.metropolis.app.model.MovieView

class ListingFacadeRecover(
    private val origin: ListingFacade
) : ListingFacade by origin, Recoverable {

    override suspend fun get() = wrap { origin.get() }.logSevere()

    override suspend fun toggle(item: MovieView) {
        origin.runCatching { toggle(item) }.logSevere()
    }

}