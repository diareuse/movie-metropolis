package movie.rating

interface ComposedRating {

    val imdb: AvailableRating?
    val rottenTomatoes: AvailableRating?
    val csfd: AvailableRating?

    val max
        get() = listOfNotNull(imdb, rottenTomatoes, csfd).maxByOrNull { it.value }

    companion object {
        val None = object : ComposedRating {
            override val imdb: AvailableRating? = null
            override val rottenTomatoes: AvailableRating? = null
            override val csfd: AvailableRating? = null
        }
    }

}