package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import movie.core.EventCinemaFeature
import movie.core.EventShowingsFeature
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.adapter.CinemaViewFromFeature
import java.util.Date
import kotlin.time.Duration.Companion.days

class TicketFacadeCinemaFromFeature(
    private val id: String,
    private val cinemas: EventCinemaFeature,
    private val showings: EventShowingsFeature.Cinema
) : TicketFacade {

    private val cinema = flow {
        val cinema = cinemas.get(null).getOrDefault(emptySequence()).first { it.id == id }
        emit(cinema)
    }.shareIn(GlobalScope, SharingStarted.Lazily, replay = 1)

    override val times: Flow<List<LazyTimeView>> = flow {
        val startTime = Date().time
        val day = 1.days
        val items = List(7) {
            val offset = (day * it).inWholeMilliseconds
            LazyTimeViewCinema(Date(startTime + offset), showings)
        }
        emit(items)
    }
    override val poster: Flow<String?> = cinema.map { it.image }
    override val name: Flow<String> = cinema.map { CinemaViewFromFeature(it).name }

}