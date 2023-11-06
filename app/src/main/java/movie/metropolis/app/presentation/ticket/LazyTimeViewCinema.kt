package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.core.EventShowingsFeature
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.TimeView
import movie.metropolis.app.model.adapter.TimeViewMovieFromFeature
import java.util.Date

class LazyTimeViewCinema(
    private val date: Date,
    showings: EventShowingsFeature.Cinema
) : LazyTimeView {
    override val content: Flow<List<TimeView>> = flow {
        val showings = showings.get(date).getOrThrow().map { (movie, showings) ->
            TimeViewMovieFromFeature(movie, showings)
        }
        emit(showings)
    }
}