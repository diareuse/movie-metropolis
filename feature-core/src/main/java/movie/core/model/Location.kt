package movie.core.model

data class Location(
    val latitude: Double,
    val longitude: Double
) {

    companion object {
        val Zero = Location(0.0, 0.0)
    }

}