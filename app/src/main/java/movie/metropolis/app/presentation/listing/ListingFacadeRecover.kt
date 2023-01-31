package movie.metropolis.app.presentation.listing

import movie.core.Recoverable
import movie.core.ResultCallback
import movie.log.logSevere
import movie.metropolis.app.model.MovieView

class ListingFacadeRecover(
    private val origin: ListingFacade
) : ListingFacade by origin, Recoverable {

    override suspend fun get(callback: ResultCallback<ListingFacade.Action>) {
        runCatchingResult(callback) {
            origin.get(it)
        }
    }

    override suspend fun toggle(item: MovieView) {
        origin.runCatching { toggle(item) }.logSevere()
    }

}