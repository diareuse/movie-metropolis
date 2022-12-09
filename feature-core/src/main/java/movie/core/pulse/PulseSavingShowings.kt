package movie.core.pulse

import movie.core.EventFeature
import movie.pulse.Pulse
import java.util.Date

class PulseSavingShowings(
    private val event: EventFeature
) : Pulse {

    override suspend fun execute() = event.getCinemas(null).onSuccess { cinemas ->
        for (cinema in cinemas)
            event.getShowings(cinema, Date())
    }

}