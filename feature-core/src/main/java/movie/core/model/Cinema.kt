package movie.core.model

interface Cinema {
    val id: String
    val name: String
    val description: String
    val city: String
    val address: Iterable<String>
    val location: Location
    val distance: Double?
    val image: String?
}