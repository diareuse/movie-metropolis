package movie.core.adapter

import movie.core.db.model.CinemaStored
import movie.core.model.Cinema
import movie.core.model.Location

data class CinemaFromDatabase(
    private val cinema: CinemaStored
) : Cinema {
    override val id: String
        get() = cinema.id
    override val name: String
        get() = cinema.name
    override val description: String
        get() = cinema.description
    override val city: String
        get() = cinema.city
    override val address: Iterable<String>
        get() = cinema.address
    override val location: Location
        get() = Location(cinema.latitude, cinema.longitude)
    override val distance: Double?
        get() = null
}