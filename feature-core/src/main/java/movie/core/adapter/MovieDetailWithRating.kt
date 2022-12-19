package movie.core.adapter

import movie.core.db.model.MovieRatingStored
import movie.core.model.MovieDetail

internal data class MovieDetailWithRating(
    private val origin: MovieDetail,
    private val stored: MovieRatingStored
) : MovieDetail by origin {

    override val rating: Byte
        get() = stored.rating
    override val linkImdb: String?
        get() = stored.linkImdb
    override val linkRottenTomatoes: String?
        get() = stored.linkRottenTomatoes
    override val linkCsfd: String?
        get() = stored.linkCsfd

}