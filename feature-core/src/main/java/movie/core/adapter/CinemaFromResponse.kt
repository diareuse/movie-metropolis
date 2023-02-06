package movie.core.adapter

import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.nwk.EndpointProvider

internal data class CinemaFromResponse(
    private val response: movie.core.nwk.model.CinemaResponse,
    override val distance: Double? = null,
    private val provider: EndpointProvider
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
    override val image: String
        get() = response.image(provider)

}