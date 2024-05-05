package movie.cinema.city.adapter

import movie.cinema.city.Cinema
import movie.cinema.city.model.CinemaResponse
import java.net.URL

internal data class CinemaFromResponse(
    private val response: CinemaResponse
) : Cinema {
    override val id: String
        get() = response.id
    override val name: String
        get() = response.name
    override val image: URL
        get() = URL(response.image)
    override val description: String
        get() = response.description
    override val address: Cinema.Address = Address()

    private inner class Address : Cinema.Address {
        override val postalCode: String
            get() = response.postalCode
        override val address: List<String>
            get() = buildList {
                this += response.addressLine
                this += response.addressLine2 ?: return@buildList
            }
        override val latitude: Double
            get() = response.latitude
        override val longitude: Double
            get() = response.longitude
        override val city: String
            get() = response.city
    }
}