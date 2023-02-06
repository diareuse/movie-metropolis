package movie.metropolis.app.model.adapter

import movie.core.model.Cinema
import movie.metropolis.app.model.CinemaView

data class CinemaViewFromFeature(
    private val cinema: Cinema
) : CinemaView {
    override val id: String
        get() = cinema.id
    override val name: String
        get() = cinema.name.substringAfterLast(',').trim()
    override val address: List<String>
        get() = cinema.address.toList()
    override val city: String
        get() = cinema.city
    override val distance: String?
        get() = cinema.distance?.let {
            when {
                it < 1.0 -> "%.2f km".format(it)
                it < 10.0 -> "%.1f km".format(it)
                else -> "%.0f km".format(it)
            }
        }
    override val image: String?
        get() = cinema.image
}