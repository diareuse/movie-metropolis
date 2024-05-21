package movie.metropolis.app.presentation.listing

import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.cinema.city.CinemaCity
import movie.metropolis.app.model.ListingView
import movie.metropolis.app.model.adapter.MovieViewFromMovie
import movie.metropolis.app.util.retryOnNetworkError

class ListingFacadeCinemaCity(
    private val future: Boolean,
    private val cinemaCity: CinemaCity
) : ListingFacade {

    override fun get(): Flow<ListingView> = flow {
        val events = cinemaCity.events.getEvents(future)
            .map(::MovieViewFromMovie).toImmutableList().let(::ListingView)
        emit(events)
    }.retryOnNetworkError()

}