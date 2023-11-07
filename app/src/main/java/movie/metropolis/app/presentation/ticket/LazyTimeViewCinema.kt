package movie.metropolis.app.presentation.ticket

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import movie.core.EventShowingsFeature
import movie.metropolis.app.model.LazyTimeView
import movie.metropolis.app.model.TimeView
import movie.metropolis.app.model.adapter.TimeViewMovieFromFeature
import java.util.Date

class LazyTimeViewCinema(
    override val date: Date,
    showings: EventShowingsFeature.Cinema
) : LazyTimeView {
    override val content: Flow<List<TimeView>> = flow {
        val showings = showings.get(date).getOrThrow().map { (movie, showings) ->
            TimeViewMovieFromFeature(movie, showings)
        }
        emit(showings)
    }.shareIn(GlobalScope, SharingStarted.Lazily)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LazyTimeViewCinema) return false

        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        return date.hashCode()
    }

}