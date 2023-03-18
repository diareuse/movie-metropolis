package movie.core.pulse

import movie.core.EventPreviewFeature
import movie.pulse.Pulse

class PulseSavingUpcoming(
    private val event: EventPreviewFeature.Factory
) : Pulse {

    override suspend fun execute() = event.upcoming().get()

}