package movie.cinema.city

import java.net.URL

interface Cinema {
    val id: String
    val name: String
    val image: URL
    val description: String
    val address: Address

    interface Address {
        val postalCode: String
        val address: List<String>
        val latitude: Double
        val longitude: Double
        val city: String
    }
}