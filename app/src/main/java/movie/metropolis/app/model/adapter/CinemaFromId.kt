package movie.metropolis.app.model.adapter

import movie.core.model.Cinema
import movie.core.model.Location

class CinemaFromId(
    override val id: String,
) : Cinema {

    override val name: String
        get() = ""
    override val description: String
        get() = ""
    override val city: String
        get() = ""
    override val address: Iterable<String>
        get() = emptyList()
    override val location: Location
        get() = Location(0.0, 0.0)
    override val distance: Double?
        get() = null
    override val image: String?
        get() = null

}