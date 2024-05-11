package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import movie.cinema.city.CinemaCity
import movie.cinema.city.parallelMap
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.TimeView
import movie.metropolis.app.model.adapter.TimeViewCinemaFromFeature
import java.util.Date

class LazyTimeViewMovie(
    private val id: String,
    override val date: Date,
    cinemaCity: CinemaCity
) : LazyTimeView {
    override val content: Flow<List<TimeView>> = flow {
        val cinemas = cinemaCity.cinemas.getCinemas()
        val showings = cinemas.parallelMap { cinema ->
            val occurrences = cinemaCity.events.getEvents(cinema, date)
                .filterKeys { it.id == id }.values.flatten()
            TimeViewCinemaFromFeature(cinema, occurrences)
        }
        emit(showings)
    }.shareIn(GlobalScope, SharingStarted.Lazily, 1)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LazyTimeViewMovie) return false

        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }

}