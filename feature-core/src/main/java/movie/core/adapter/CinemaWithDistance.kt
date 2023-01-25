package movie.core.adapter

import movie.core.model.Cinema

data class CinemaWithDistance(
    private val origin: Cinema,
    override val distance: Double?
) : Cinema by origin