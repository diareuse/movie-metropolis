package movie.core.adapter

import movie.core.model.Cinema

class CinemaWithDistance(
    origin: Cinema,
    override val distance: Double?
) : Cinema by origin