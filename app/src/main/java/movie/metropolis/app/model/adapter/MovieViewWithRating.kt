package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.MovieView

data class MovieViewWithRating(
    private val origin: MovieView,
    override val rating: String?
) : MovieView by origin {

    constructor(
        origin: MovieView,
        rating: Byte
    ) : this(
        origin,
        if (rating <= 0) null else "%d%%".format(rating)
    )

}