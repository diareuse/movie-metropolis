package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.ListingView

interface ListingFacade {

    fun get(): Flow<ListingView>

    interface Factory {

        fun upcoming(): ListingFacade
        fun current(): ListingFacade

    }

}