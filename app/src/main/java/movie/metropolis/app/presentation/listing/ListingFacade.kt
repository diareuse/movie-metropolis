package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.ListingView
import movie.metropolis.app.model.MovieView

interface ListingFacade {

    fun get(): Flow<ListingView>
    suspend fun toggle(item: MovieView)

    interface Factory {

        fun upcoming(): ListingFacade
        fun current(): ListingFacade

    }

}