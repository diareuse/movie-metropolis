package movie.metropolis.app.screen.listing

import kotlinx.coroutines.flow.flow
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable

interface ListingFacade {

    suspend fun getCurrent(): Result<List<MovieView>>
    suspend fun getUpcoming(): Result<List<MovieView>>

    companion object {

        val ListingFacade.currentFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getCurrent().asLoadable())
            }

        val ListingFacade.upcomingFlow
            get() = flow {
                emit(Loadable.loading())
                emit(getUpcoming().asLoadable())
            }

    }

}