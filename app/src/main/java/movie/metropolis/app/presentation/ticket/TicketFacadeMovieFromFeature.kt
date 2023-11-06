package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.core.EventDetailFeature
import movie.core.EventShowingsFeature
import movie.core.adapter.MovieFromId
import movie.metropolis.app.model.LazyTimeView
import java.util.Date
import kotlin.math.max
import kotlin.time.Duration.Companion.days

class TicketFacadeMovieFromFeature(
    private val movie: String,
    private val detail: EventDetailFeature,
    private val showings: EventShowingsFeature.Movie,
) : TicketFacade {

    override val times: Flow<List<LazyTimeView>> = flow {
        val detail = detail.get(MovieFromId(movie)).getOrThrow()
        val startTime = max(Date().time, detail.screeningFrom.time)
        val day = 1.days
        val items = List(14) {
            val offset = (day * it).inWholeMilliseconds
            LazyTimeViewMovie(Date(startTime + offset), showings)
        }
        emit(items)
    }

}