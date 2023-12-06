package movie.core.pulse

import movie.core.EventCinemaFeature
import movie.core.EventShowingsFeature
import movie.pulse.Pulse
import java.util.Date

class PulseSavingShowings(
    private val cinema: EventCinemaFeature,
    private val showing: EventShowingsFeature.Factory
) : Pulse {

    // todo try loading for release dates of movies user has marked as favorite
    override suspend fun execute() = cinema.runCatching { get(null) }.onSuccess { cinemas ->
        for (cinema in cinemas)
            showing.cinema(cinema).get(Date())
    }

}