package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.core.EventShowingsFeature
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.TimeView
import movie.metropolis.app.model.adapter.TimeViewCinemaFromFeature
import java.util.Date

class LazyTimeViewMovie(
    private val date: Date,
    showings: EventShowingsFeature.Movie
) : LazyTimeView {
    override val content: Flow<List<TimeView>> = flow {
        val showings = showings.get(date).getOrThrow().map { (cinema, showings) ->
            TimeViewCinemaFromFeature(cinema, showings)
        }
        emit(showings)
    }

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