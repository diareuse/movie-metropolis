package movie.metropolis.app.screen.listing

import movie.core.Recoverable
import movie.core.ResultCallback
import movie.log.logSevere
import movie.metropolis.app.model.MovieView

class ListingFacadeRecover(
    private val origin: ListingFacade
) : ListingFacade by origin, Recoverable {

    override suspend fun getCurrent(callback: ResultCallback<List<MovieView>>) {
        runCatchingResult(callback) {
            origin.getCurrent { result ->
                it(result.logSevere())
            }
        }
    }

    override suspend fun getUpcoming(callback: ResultCallback<List<MovieView>>) {
        runCatchingResult(callback) {
            origin.getUpcoming { result ->
                it(result.logSevere())
            }
        }
    }

    override suspend fun toggleFavorite(movie: MovieView) {
        origin.runCatching { toggleFavorite(movie) }.logSevere()
    }

}