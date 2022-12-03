package movie.metropolis.app.feature.global.model.adapter

import movie.metropolis.app.feature.global.model.Cinema
import movie.metropolis.app.feature.global.model.Location
import movie.metropolis.app.feature.global.model.remote.CinemaResponse

internal data class CinemaFromResponse(
    private val response: CinemaResponse,
    override val distance: Double? = null
) : Cinema {

    override val id: String
        get() = response.id
    override val name: String
        get() = response.name
    override val description: String
        get() = response.description
    override val city: String
        get() = response.city
    override val address: Iterable<String>
        get() = listOfNotNull(response.addressLine, response.addressLine2)
    override val location: Location
        get() = Location(response.latitude, response.longitude)

}