package movie.core.pulse

import movie.core.EventFeature
import movie.pulse.Pulse

class PulseSavingCurrent(
    private val event: EventFeature
) : Pulse {

    override suspend fun execute() = event.getCurrent()

}