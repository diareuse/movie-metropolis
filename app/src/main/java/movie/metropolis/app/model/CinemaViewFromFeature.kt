package movie.metropolis.app.model

import movie.metropolis.app.feature.global.Cinema

data class CinemaViewFromFeature(
    private val cinema: Cinema
) : CinemaView {
    override val id: String
        get() = cinema.id
    override val name: String
        get() = cinema.name
    override val address: List<String>
        get() = cinema.address.toList()
    override val city: String
        get() = cinema.city
    override val distance: String?
        get() = cinema.distance?.let { "%.2fkm".format(it) }
}