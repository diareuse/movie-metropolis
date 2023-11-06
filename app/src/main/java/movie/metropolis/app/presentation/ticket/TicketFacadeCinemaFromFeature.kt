package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.core.EventShowingsFeature
import movie.metropolis.app.model.LazyTimeView
import java.util.Date
import kotlin.time.Duration.Companion.days

class TicketFacadeCinemaFromFeature(
    private val showings: EventShowingsFeature.Cinema
) : TicketFacade {

    override val times: Flow<List<LazyTimeView>> = flow {
        val startTime = Date().time
        val day = 1.days
        val items = List(14) {
            val offset = (day * it).inWholeMilliseconds
            LazyTimeViewCinema(Date(startTime + offset), showings)
        }
        emit(items)
    }

}