package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.flow.Flow
import movie.core.model.Location
import movie.metropolis.app.model.LazyTimeView

interface TicketFacade {

    val times: Flow<List<LazyTimeView>>

    fun interface LocationFactory {
        fun create(location: Location): TicketFacade
    }

    interface Factory {
        fun movie(id: String): LocationFactory
        fun cinema(id: String): LocationFactory
    }

}

