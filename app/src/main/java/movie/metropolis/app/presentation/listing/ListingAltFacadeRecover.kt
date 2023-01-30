package movie.metropolis.app.presentation.listing

import movie.core.Recoverable
import movie.core.ResultCallback

class ListingAltFacadeRecover(
    private val origin: ListingAltFacade
) : ListingAltFacade, Recoverable {

    override suspend fun get(callback: ResultCallback<ListingAltFacade.Action>) {
        runCatchingResult(callback) {
            origin.get(it)
        }
    }

}