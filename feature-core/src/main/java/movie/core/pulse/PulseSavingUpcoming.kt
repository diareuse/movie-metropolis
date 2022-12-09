package movie.core.pulse

import movie.core.EventFeature
import movie.pulse.Pulse

class PulseSavingUpcoming(
    private val event: EventFeature
) : Pulse {

    override suspend fun execute() = event.getUpcoming()

}