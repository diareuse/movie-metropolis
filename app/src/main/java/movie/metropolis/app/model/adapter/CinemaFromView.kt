package movie.metropolis.app.model.adapter

import movie.metropolis.app.feature.global.model.Cinema
import movie.metropolis.app.feature.global.model.Location
import movie.metropolis.app.model.CinemaView

data class CinemaFromView(
    private val cinema: CinemaView
) : Cinema {
    override val id: String
        get() = cinema.id
    override val name: String
        get() = cinema.name
    override val description: String
        get() = ""
    override val city: String
        get() = cinema.city
    override val address: Iterable<String>
        get() = cinema.address
    override val location: Location
        get() = Location(0.0, 0.0)
    override val distance: Double?
        get() = null
}