package movie.metropolis.app.feature.global

interface Cinema {
    val id: String
    val name: String
    val description: String
    val city: String
    val address: Iterable<String>
    val location: Location
    val distance: Double?
}