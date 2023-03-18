package movie.core.pulse

import movie.core.EventPreviewFeature
import movie.pulse.Pulse

class PulseSavingCurrent(
    private val event: EventPreviewFeature.Factory
) : Pulse {

    override suspend fun execute() = event.current().get()

}