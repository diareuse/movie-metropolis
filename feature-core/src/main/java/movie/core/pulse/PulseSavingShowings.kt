package movie.core.pulse

import movie.core.EventCinemaFeature
import movie.core.EventCinemaFeature.Companion.get
import movie.core.EventShowingsFeature
import movie.core.EventShowingsFeature.Cinema.Companion.get
import movie.pulse.Pulse
import java.util.Date

class PulseSavingShowings(
    private val cinema: EventCinemaFeature,
    private val showing: EventShowingsFeature.Factory
) : Pulse {

    // todo try loading for release dates of movies user has marked as favorite
    override suspend fun execute() = cinema.get(null).onSuccess { cinemas ->
        for (cinema in cinemas)
            showing.cinema(cinema).get(Date())
    }

}