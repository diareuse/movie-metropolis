package movie.metropolis.app.presentation.listing

import movie.core.Recoverable
import movie.core.ResultCallback
import movie.log.logSevere
import movie.metropolis.app.model.MovieView

class ListingAltFacadeRecover(
    private val origin: ListingAltFacade
) : ListingAltFacade by origin, Recoverable {

    override suspend fun get(callback: ResultCallback<ListingAltFacade.Action>) {
        runCatchingResult(callback) {
            origin.get(it)
        }
    }

    override suspend fun toggle(item: MovieView) {
        origin.runCatching { toggle(item) }.logSevere()
    }

}