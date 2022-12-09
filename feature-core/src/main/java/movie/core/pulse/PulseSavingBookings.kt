package movie.core.pulse

import movie.core.UserFeature
import movie.pulse.Pulse

class PulseSavingBookings(
    private val user: UserFeature
) : Pulse {

    override suspend fun execute() = user.getBookings()

}