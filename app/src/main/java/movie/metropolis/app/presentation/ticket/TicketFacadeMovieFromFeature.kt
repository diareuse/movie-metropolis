package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import movie.core.EventDetailFeature
import movie.core.EventShowingsFeature
import movie.core.adapter.MovieFromId
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.adapter.MovieDetailViewFromFeature
import java.util.Date
import kotlin.math.max
import kotlin.time.Duration.Companion.days

class TicketFacadeMovieFromFeature(
    private val id: String,
    private val detail: EventDetailFeature,
    private val showings: EventShowingsFeature.Movie,
) : TicketFacade {

    private val movie = flow {
        val detail = detail.get(MovieFromId(id)).getOrThrow()
        emit(detail)
    }.shareIn(GlobalScope, SharingStarted.Lazily, replay = 1)

    override val times: Flow<List<LazyTimeView>> = movie.map { detail ->
        val startTime = max(Date().time, detail.screeningFrom.time)
        val day = 1.days
        List(14) {
            val offset = (day * it).inWholeMilliseconds
            LazyTimeViewMovie(Date(startTime + offset), showings)
        }
    }

    override val poster: Flow<String?> = movie.map {
        MovieDetailViewFromFeature(it).poster?.url
    }

}